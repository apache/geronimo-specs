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
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

public abstract class JsonProvider {
    private static final String DEFAULT_PROVIDER = "org.apache.json.JsonProviderImpl";

    protected JsonProvider() {
        // no-op
    }

    public static JsonProvider provider() {
        if (System.getSecurityManager() != null) {
            return AccessController.doPrivileged(new PrivilegedAction<JsonProvider>() {
                public JsonProvider run() {
                    return doLoadProvider();
                }
            });
        }
        return doLoadProvider();
    }

    private static JsonProvider doLoadProvider() throws JsonException {
        final ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        try {
            final Class<?> clazz = Class.forName("org.apache.geronimo.osgi.locator.ProviderLocator");
            final Method getServices = clazz.getDeclaredMethod("getServices", String.class, Class.class, ClassLoader.class);
            final List<JsonProvider> osgiProviders = (List<JsonProvider>) getServices.invoke(null, JsonProvider.class.getName(), JsonProvider.class, tccl);
            if (osgiProviders != null && !osgiProviders.isEmpty()) {
                return osgiProviders.iterator().next();
            }
        } catch (final Throwable e) {
            // locator not available, try normal mode
        }

        final Iterator<JsonProvider> serviceLoader = ServiceLoader.load(JsonProvider.class, tccl).iterator();
        if (serviceLoader.hasNext()) {
            return serviceLoader.next();
        }

        try {
            final Class<?> clazz = Class.forName(DEFAULT_PROVIDER, true, tccl);
            return JsonProvider.class.cast(clazz.newInstance());
        } catch (final Throwable cnfe) {
            throw new JsonException(DEFAULT_PROVIDER + " not found", cnfe);
        }
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
}

