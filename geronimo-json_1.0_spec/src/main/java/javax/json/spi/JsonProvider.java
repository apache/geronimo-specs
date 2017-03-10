/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package javax.json.spi;

import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonException;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;

import org.apache.geronimo.osgi.locator.ProviderLocator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class JsonProvider {
    private static final String DEFAULT_PROVIDER = "org.apache.johnzon.core.JsonProviderImpl";

    private static final Cache CACHE = new Cache();

    protected JsonProvider() {
        // no-op
    }

    public static JsonProvider provider() {
        return CACHE.get();
    }

    public abstract JsonParser createParser(Reader reader);

    public abstract JsonParser createParser(InputStream in);

    public abstract JsonParserFactory createParserFactory(Map<String, ?> config);

    public abstract JsonGenerator createGenerator(Writer writer);

    public abstract JsonGenerator createGenerator(OutputStream out);

    public abstract JsonGeneratorFactory createGeneratorFactory(Map<String, ?> config);

    public abstract JsonReader createReader(Reader reader);

    public abstract JsonReader createReader(InputStream in);

    public abstract JsonWriter createWriter(Writer writer);

    public abstract JsonWriter createWriter(OutputStream out);

    public abstract JsonWriterFactory createWriterFactory(Map<String, ?> config);

    public abstract JsonReaderFactory createReaderFactory(Map<String, ?> config);

    public abstract JsonObjectBuilder createObjectBuilder();

    public abstract JsonArrayBuilder createArrayBuilder();

    public abstract JsonBuilderFactory createBuilderFactory(Map<String, ?> config);

    private static class Cache {
        private final WeakHashMap<ClassLoader, WeakReference<JsonProvider>> cachingProviders = new WeakHashMap<ClassLoader, WeakReference<JsonProvider>>();
        private final ReadWriteLock lock = new ReentrantReadWriteLock();

        private JsonProvider get() { // in term of synchro we don't prevent to load multiple times the provider
            ClassLoader key = Thread.currentThread().getContextClassLoader();
            if (key == null) {
                key = ClassLoader.getSystemClassLoader();
            }

            final WeakReference<JsonProvider> reference;
            final Lock readLock = this.lock.readLock();
            readLock.lock();
            try {
                reference = cachingProviders.get(key);
            } finally {
                readLock.unlock();
            }

            JsonProvider provider = null;
            if (reference != null) {
                provider = reference.get();
            }
            if (provider != null) {
                return provider;
            }

            if (System.getSecurityManager() != null) {
                provider = AccessController.doPrivileged(new PrivilegedAction<JsonProvider>() {
                    public JsonProvider run() {
                        return doLoadProvider();
                    }
                });
            } else {
                provider = doLoadProvider();
            }

            final Lock writeLock = this.lock.writeLock();
            writeLock.lock();
            try {
                boolean put = true;
                final WeakReference<JsonProvider> existing = cachingProviders.get(key);
                if (existing != null) {
                    final JsonProvider p = existing.get();
                    if (p != null) {
                        provider = p;
                        put = false;
                    }
                }
                if (put) {
                    cachingProviders.put(key, new WeakReference<JsonProvider>(provider));
                }
            } finally {
                writeLock.unlock();
            }

            return provider;
        }

        private static JsonProvider doLoadProvider() throws JsonException {
            ClassLoader tccl = Thread.currentThread().getContextClassLoader();
            if (tccl == null) {
                tccl = ClassLoader.getSystemClassLoader();
            }
            try {
                @SuppressWarnings("unchecked")
                final List<JsonProvider> osgiProviders = (List)ProviderLocator.getServices(JsonProvider.class.getName(), JsonProvider.class, tccl);
                if (osgiProviders != null && !osgiProviders.isEmpty()) {
                    return osgiProviders.iterator().next();
                }
            } catch (final Throwable e) {
                // locator not available, try normal mode
            }

            final String className = System.getProperty(JsonProvider.class.getName());
            if (className != null) {
                try {
                    return JsonProvider.class.cast(tccl.loadClass(className.trim()).newInstance());
                } catch (final Exception e) {
                    throw new JsonException("Specified provider as system property can't be loaded: " + className, e);
                }
            }

            // don't use Class.forName() to avoid to bind class to tccl if thats a classloader facade
            // so implementing a simple SPI when ProviderLocator is not here
            final String name = "META-INF/services/" + JsonProvider.class.getName();
            try {
                final Enumeration<URL> configs = tccl.getResources(name);
                if (configs.hasMoreElements()) {
                    InputStream in = null;
                    BufferedReader r = null;
                    try {
                        in = configs.nextElement().openStream();
                        r = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String l;
                        while ((l = r.readLine()) != null) {
                            if (l.startsWith("#")) {
                                continue;
                            }
                            return JsonProvider.class.cast(tccl.loadClass(l).newInstance());
                        }
                    } catch (final IOException x) {
                        // no-op
                    } finally {
                        try {
                            if (r != null) {
                                r.close();
                            }
                        } catch (final IOException y) {
                            // no-op
                        }
                        try {
                            if (in != null) {
                                in.close();
                            }
                        } catch (final IOException y) {
                            // no-op
                        }
                    }
                }
            } catch (final Exception ex) {
                // no-op
            }

            try {
                return JsonProvider.class.cast(tccl.loadClass(DEFAULT_PROVIDER).newInstance());
            } catch (final Throwable cnfe) {
                throw new JsonException(DEFAULT_PROVIDER + " not found", cnfe);
            }
        }
    }
}

