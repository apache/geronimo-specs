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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
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

            // try to load provider specified via system property
            final String className = System.getProperty(JsonProvider.class.getName());
            if (className != null) {
                try {
                    return JsonProvider.class.cast(tccl.loadClass(className.trim()).newInstance());
                } catch (final Exception e) {
                    throw new JsonException("Specified provider as system property can't be loaded: " + className, e);
                }
            }

            // try to load via ServiceLoader (as registered in META-INF/services)
            Iterator<JsonProvider> providers = ServiceLoader.load(JsonProvider.class).iterator();
            if (providers.hasNext()) {
                return providers.next();
            }

            // try to load to default provider
            try {
                return JsonProvider.class.cast(tccl.loadClass(DEFAULT_PROVIDER).newInstance());
            } catch (final Throwable cnfe) {
                throw new JsonException(DEFAULT_PROVIDER + " not found", cnfe);
            }
        }
    }
}
