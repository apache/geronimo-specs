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

public final class Json
{
    private Json()
    {
        // no-op
    }

    public static JsonParser createParser(Reader reader)
    {
        return JsonProvider.provider().createParser(reader);
    }

    public static JsonParser createParser(InputStream in)
    {
        return JsonProvider.provider().createParser(in);
    }

    public static JsonGenerator createGenerator(Writer writer)
    {
        return JsonProvider.provider().createGenerator(writer);
    }

    public static JsonGenerator createGenerator(OutputStream out)
    {
        return JsonProvider.provider().createGenerator(out);
    }

    public static JsonParserFactory createParserFactory(Map<String, ?> config)
    {
        return JsonProvider.provider().createParserFactory(config);
    }

    public static JsonGeneratorFactory createGeneratorFactory(Map<String, ?> config)
    {
        return JsonProvider.provider().createGeneratorFactory(config);
    }

    public static JsonWriter createWriter(Writer writer)
    {
        return JsonProvider.provider().createWriter(writer);
    }

    public static JsonWriter createWriter(OutputStream out)
    {
        return JsonProvider.provider().createWriter(out);
    }

    public static JsonReader createReader(Reader reader)
    {
        return JsonProvider.provider().createReader(reader);
    }

    public static JsonReader createReader(InputStream in)
    {
        return JsonProvider.provider().createReader(in);
    }

    public static JsonReaderFactory createReaderFactory(Map<String, ?> config)
    {
        return JsonProvider.provider().createReaderFactory(config);
    }

    public static JsonWriterFactory createWriterFactory(Map<String, ?> config)
    {
        return JsonProvider.provider().createWriterFactory(config);
    }

    public static JsonArrayBuilder createArrayBuilder()
    {
        return JsonProvider.provider().createArrayBuilder();
    }

    /**
     * Create an empty JsonObjectBuilder
     *
     * @since 1.0
     */
    public static JsonObjectBuilder createObjectBuilder()
    {
        return JsonProvider.provider().createObjectBuilder();
    }

    /**
     * Creates a JSON object builder, initialized with the specified JsonObject.
     *
     * @since 1.1
     */
    public static JsonObjectBuilder createObjectBuilder(JsonObject object)
    {
        return JsonProvider.provider().createObjectBuilder(object);
    }

    /**
     * Creates a JSON object builder, initialized with the specified Map.
     *
     * @since 1.1
     */
    public static JsonObjectBuilder createObjectBuilder(Map<String, Object> map)
    {
        return JsonProvider.provider().createObjectBuilder(map);
    }

    public static JsonBuilderFactory createBuilderFactory(Map<String, ?> config)
    {
        return JsonProvider.provider().createBuilderFactory(config);
    }

    /**
     * Creates a JSON array builder, initialized with an initial content
     * taken from a JsonArray
     *
     * @param initialData the initial array in the builder
     * @return a JSON array builder
     * @since 1.1
     */
    public static JsonArrayBuilder createArrayBuilder(JsonArray initialData)
    {
        return JsonProvider.provider().createArrayBuilder(initialData);
    }

    /**
     * Creates a JSON array builder, initialized with an initial content
     *
     * @param initialData the initial array in the builder
     * @return a JSON array builder
     * @since 1.1
     */
    public static JsonArrayBuilder createArrayBuilder(Collection<?> initialData)
    {
        return JsonProvider.provider().createArrayBuilder(initialData);
    }

    public static JsonString createValue(String value)
    {
        return JsonProvider.provider().createValue(value);
    }

    public static JsonNumber createValue(int value)
    {
        return JsonProvider.provider().createValue(value);
    }

    public static JsonNumber createValue(long value)
    {
        return JsonProvider.provider().createValue(value);
    }

    public static JsonNumber createValue(double value)
    {
        return JsonProvider.provider().createValue(value);
    }

    public static JsonNumber createValue(BigDecimal value)
    {
        return JsonProvider.provider().createValue(value);
    }

    public static JsonNumber createValue(BigInteger value)
    {
        return JsonProvider.provider().createValue(value);
    }

    /**
     * Create a {@link JsonPatch} as defined in
     * <a href="https://tools.ietf.org/html/rfc6902">RFC-6902</a>.
     *
     * @param array with the patch operations
     * @return the JsonPatch based on the given operations
     * @since 1.1
     */
    public static JsonPatch createPatch(JsonArray array)
    {
        return JsonProvider.provider().createPatch(array);
    }

    /**
     * Create a {@link JsonPatch} by comparing the source to the target as defined in
     * <a href="https://tools.ietf.org/html/rfc6902">RFC-6902</a>.
     * <p>
     * Applying this {@link JsonPatch} to the source you will give you the target.
     *
     * @see #createPatch(JsonArray)
     * @since 1.1
     */
    public static JsonPatch createDiff(JsonStructure source, JsonStructure target)
    {
        return JsonProvider.provider().createDiff(source, target);
    }

    /**
     * Create a new JsonPatchBuilder
     *
     * @since 1.1
     */
    public static JsonPatchBuilder createPatchBuilder()
    {
        return JsonProvider.provider().createPatchBuilder();
    }

    /**
     * Create a new JsonPatchBuilder
     *
     * @param initialData the initial patch operations
     * @since 1.1
     */
    public static JsonPatchBuilder createPatchBuilder(JsonArray initialData)
    {
        return JsonProvider.provider().createPatchBuilder(initialData);
    }

    /**
     * Creates JSON Merge Patch (<a href="http://tools.ietf.org/html/rfc7396">RFC 7396</a>)
     * from a specified {@link JsonValue}.
     * Create a merged patch by comparing the source to the target.
     * <p>
     * Applying this JsonPatch to the source will give you the target.
     * <p>
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
     * <p>
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
     * @param patch the patch
     * @return a JSON Merge Patch
     * @since 1.1
     */
    public static JsonMergePatch createMergePatch(JsonValue patch)
    {
        return JsonProvider.provider().createMergePatch(patch);
    }

    /**
     * Create a JSON Merge Patch (<a href="http://tools.ietf.org/html/rfc7396">RFC 7396</a>)
     * from the source and target {@link JsonValue JsonValues}.
     * Create a merged patch by comparing the source to the target.
     * <p>
     * Applying this JsonPatch to the source will give you the target.
     * <p>
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
     * <p>
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
     * @param source the source
     * @param target the target
     * @return a JSON Merge Patch
     * @since 1.1
     */
    public static JsonMergePatch createMergeDiff(JsonValue source, JsonValue target)
    {
        return JsonProvider.provider().createMergeDiff(source, target);
    }

    /**
     * Create a {@link JsonPointer} for the given path
     *
     * @since 1.1
     */
    public static JsonPointer createPointer(String path)
    {
        return JsonProvider.provider().createPointer(path);
    }

    /**
     * @param pointer to encode
     * @return the properly encoded JsonPointer string
     * @since 1.1
     */
    public static String encodePointer(String pointer)
    {
        if (pointer == null || pointer.length() == 0)
        {
            return pointer;
        }

        return pointer.replace("~", "~0").replace("/", "~1");
    }

    /**
     * @param escapedPointer
     * @return the de-escaped JsonPointer
     *
     * @since 1.1
     */
    public static String decodePointer(String escapedPointer)
    {
        if (escapedPointer == null || escapedPointer.length() == 0)
        {
            return escapedPointer;
        }

        return escapedPointer.replace("~1", "/").replace("~0", "~");
    }
}
