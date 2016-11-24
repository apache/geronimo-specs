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

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonException;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonPatch;
import javax.json.JsonPatchBuilder;
import javax.json.JsonPointer;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * JsonProvider is the actual implementation of all the Json logic.
 * A JsonProvider instance can be used concurrently.
 *
 * It can be accessed via
 * <pre>
 *     <code>
 * JsonProvider provider = JsonProvider.provider();
 *     </code>
 * </pre>
 */
public abstract class JsonProvider {
    private static final String DEFAULT_PROVIDER = "org.apache.johnzon.core.JsonProviderImpl";

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

        // don't use Class.forName() to avoid to bind class to tccl if thats a classloader facade
        // so implementing a simple SPI when ProviderLocator is not here
        final String name = "META-INF/services/" + JsonProvider.class.getName();
        try {
            Enumeration<URL> configs;
            if (tccl == null) {
                configs = ClassLoader.getSystemResources(name);
            } else {
                configs = tccl.getResources(name);
            }

            if (configs.hasMoreElements()) {
                InputStream in = null;
                BufferedReader r = null;
                final List<String> names = new ArrayList<String>();
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
            final Class<?> clazz = tccl.loadClass(DEFAULT_PROVIDER);
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

    /**
     * Create an empty JsonObjectBuilder
     * @since 1.0
     */
    public abstract JsonObjectBuilder createObjectBuilder();

    /**
     * Creates a JSON object builder, initialized with the specified JsonObject.
     * @since 1.1
     */
    public JsonObjectBuilder createObjectBuilder(JsonObject jsonObject) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a JSON object builder, initialized with the specified Map.
     * @since 1.1
     */
    public JsonObjectBuilder createObjectBuilder(Map<String, Object> map) {
        throw new UnsupportedOperationException();
    }

    public abstract JsonArrayBuilder createArrayBuilder();

    public JsonArrayBuilder createArrayBuilder(JsonArray initialData) {
        throw new UnsupportedOperationException();
    }

    public JsonArrayBuilder createArrayBuilder(Collection<Object> initialData) {
        throw new UnsupportedOperationException();
    }

    public abstract JsonPointer createJsonPointer(String path);

    public abstract JsonBuilderFactory createBuilderFactory(Map<String, ?> config);


    public JsonString createValue(String value) {
        throw new UnsupportedOperationException();
    }

    public JsonNumber createValue(int value) {
        throw new UnsupportedOperationException();
    }

    public JsonNumber createValue(long value) {
        throw new UnsupportedOperationException();
    }

    public JsonNumber createValue(double value) {
        throw new UnsupportedOperationException();
    }

    public JsonNumber createValue(BigDecimal value) {
        throw new UnsupportedOperationException();
    }

    public JsonNumber createValue(BigInteger value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Create a JsonPatch by comparing the source to the target.
     * Applying this JsonPatch to the source you will give you the target.
     * @since 1.1
     */
    public abstract JsonPatch createPatch(JsonStructure source, JsonStructure target);

    /**
     * Create a new JsonPatchBuilder
     * @since 1.1
     */
    public abstract JsonPatchBuilder createPatchBuilder();

    /**
     * Create a new JsonPatchBuilder from initial data.
     * @param initialData the initial patch operations
     * @since 1.1
     */
    public abstract JsonPatchBuilder createPatchBuilder(JsonArray initialData);

    /**
     * Create a merged patch by comparing the source to the target.
     * Applying this JsonPatch to the source will give you the target.
     * A mergePatch is a JsonValue as defined in http://tools.ietf.org/html/rfc7396
     *
     * If you have a JSON like
     * <pre>
     * {
     *   "a": "b",
     *   "c": {
     *     "d": "e",
     *     "f": "g"
     *   }
     * }
     * </pre>
     *
     * Then you can change the value of "a" and removing "f" by sending:
     * <pre>
     * {
     *   "a":"z",
     *   "c": {
     *     "f": null
     *   }
     * }
     * </pre>
     *
     * @see #createPatch(JsonStructure, JsonStructure)
     * @see #mergePatch(JsonValue, JsonValue)
     *
     * @since 1.1
     */
    public abstract JsonValue createMergePatch(JsonValue source , JsonValue target);

    /**
     * Merge the given patch to the existing source
     * A mergePatch is a JsonValue as defined in http://tools.ietf.org/html/rfc7396
     *
     * @return the result of applying the patch to the source
     *
     * @see #createMergePatch(JsonValue, JsonValue)
     * @since 1.1
     */
    public abstract JsonValue mergePatch(JsonValue source, JsonValue patch);

}

