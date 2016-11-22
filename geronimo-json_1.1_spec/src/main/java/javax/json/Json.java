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
package javax.json;

import javax.json.spi.JsonProvider;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;

public final class Json {
    private Json() {
        // no-op
    }

    public static JsonParser createParser(Reader reader) {
        return JsonProvider.provider().createParser(reader);
    }

    public static JsonParser createParser(InputStream in) {
        return JsonProvider.provider().createParser(in);
    }

    public static JsonGenerator createGenerator(Writer writer) {
        return JsonProvider.provider().createGenerator(writer);
    }

    public static JsonGenerator createGenerator(OutputStream out) {
        return JsonProvider.provider().createGenerator(out);
    }

    public static JsonParserFactory createParserFactory(Map<String, ?> config) {
        return JsonProvider.provider().createParserFactory(config);
    }

    public static JsonGeneratorFactory createGeneratorFactory(Map<String, ?> config) {
        return JsonProvider.provider().createGeneratorFactory(config);
    }

    public static JsonWriter createWriter(Writer writer) {
        return JsonProvider.provider().createWriter(writer);
    }

    public static JsonWriter createWriter(OutputStream out) {
        return JsonProvider.provider().createWriter(out);
    }

    public static JsonReader createReader(Reader reader) {
        return JsonProvider.provider().createReader(reader);
    }

    public static JsonReader createReader(InputStream in) {
        return JsonProvider.provider().createReader(in);
    }

    public static JsonReaderFactory createReaderFactory(Map<String, ?> config) {
        return JsonProvider.provider().createReaderFactory(config);
    }

    public static JsonWriterFactory createWriterFactory(Map<String, ?> config) {
        return JsonProvider.provider().createWriterFactory(config);
    }

    public static JsonArrayBuilder createArrayBuilder() {
        return JsonProvider.provider().createArrayBuilder();
    }

    /**
     * Create an empty JsonObjectBuilder
     * @since 1.0
     */
    public static JsonObjectBuilder createObjectBuilder() {
        return JsonProvider.provider().createObjectBuilder();
    }

    /**
     * Creates a JSON object builder, initialized with the specified JsonObject.
     * @since 1.1
     */
    public static JsonObjectBuilder createObjectBuilder(JsonObject object) {
        return JsonProvider.provider().createObjectBuilder(object);
    }

    /**
     * Creates a JSON object builder, initialized with the specified Map.
     * @since 1.1
     */
    public static JsonObjectBuilder createObjectBuilder(Map<String, Object> map) {
        return JsonProvider.provider().createObjectBuilder(map);
    }

    public static JsonBuilderFactory createBuilderFactory(Map<String, ?> config) {
        return JsonProvider.provider().createBuilderFactory(config);
    }

    /**
     * Creates a JSON array builder, initialized with an initial content
     * taken from a JsonArray
     *
     * @param initialData the initial array in the builder
     * @return a JSON array builder
     *
     * @since 1.1
     */
    public static JsonArrayBuilder createArrayBuilder(JsonArray initialData) {
        return JsonProvider.provider().createArrayBuilder(initialData);
    }
    /**
     * Creates a JSON array builder, initialized with an initial content
     *
     * @param initialData the initial array in the builder
     * @return a JSON array builder
     *
     * @since 1.1
     */
    public static JsonArrayBuilder createArrayBuilder(Collection<Object> initialData) {
        return JsonProvider.provider().createArrayBuilder(initialData);
    }
    
    public static JsonString createValue(String value) {
        return JsonProvider.provider().createValue(value);
    }

    public static JsonNumber createValue(int value) {
        return JsonProvider.provider().createValue(value);
    }

    public static JsonNumber createValue(long value) {
        return JsonProvider.provider().createValue(value);
    }

    public static JsonNumber createValue(double value) {
        return JsonProvider.provider().createValue(value);
    }

    public static JsonNumber createValue(BigDecimal value) {
        return JsonProvider.provider().createValue(value);
    }

    public static JsonNumber createValue(BigInteger value) {
        return JsonProvider.provider().createValue(value);
    }

    /**
     * Create a JsonPatch by comparing the source to the target.
     * Applying this JsonPatch to the source you will give you the target.
     *
     * @see #createMergePatch(JsonValue, JsonValue)
     *
     * @since 1.1
     */
    public static JsonPatch createPatch(JsonStructure source, JsonStructure target) {
        return JsonProvider.provider().createPatch(source, target);
    }

    /**
     * Create a new JsonPatchBuilder
     * @since 1.1
     */
    public static JsonPatchBuilder createPatchBuilder() {
        return JsonProvider.provider().createPatchBuilder();
    }

    /**
     * Create a new JsonPatchBuilder
     * @since 1.1
     */
    public static JsonPatchBuilder createPatchBuilder(JsonArray initialData) {
        return JsonProvider.provider().createPatchBuilder(initialData);
    }

    /**
     * Create a merged patch by comparing the source to the target.
     * Applying this JsonPatch to the source you will give you the target.
     *
     * @see #createPatch(JsonStructure, JsonStructure)
     * @since 1.1
     */
    public static JsonValue createMergePatch(JsonValue source, JsonValue target) {
        return JsonProvider.provider().createMergePatch(source, target);
    }

    /**
     * Merge the given patch to the existing source
     * @return the result of applying the patch to the source
     * @since 1.1
     */
    public static JsonValue mergePatch(JsonValue source, JsonValue patch) {
        return JsonProvider.provider().mergePatch(source, patch);
    }
}
