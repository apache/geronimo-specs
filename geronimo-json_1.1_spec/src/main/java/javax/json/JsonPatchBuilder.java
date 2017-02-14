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

/**
 * TODO this is a final class in the spec, but that makes no sense.
 * This class can be used to easily create {@link JsonPatch}es.
 * To get an instance use {@link Json#createPatchBuilder()} or {@link Json#createPatchBuilder(JsonArray)}
 * The order of the operations corresponds to the order they are builded.
 * <p>
 * NOTICE: A JsonPatchBuilder contains state and therefore is NOT threadsafe and should not be used concurrently.
 * <p>
 * The following {@link JsonPatch}
 * <pre>
 *     "[
 *         {
 *           "op": "add",
 *           "path": "/add/object",
 *           "value": {
 *             "foo": "bar"
 *           }
 *         },
 *         {
 *           "op": "remove",
 *           "path": "/remove/it"
 *         },
 *         {
 *           "op": "move",
 *           "path": "move/to",
 *           "from": "move/from"
 *         }
 *     ]"
 * </pre>
 * can be build with the JsonPatchBuilder
 * <pre>
 *
 *     JsonPatch patch = Json.createJsonPatchBuilder()
 *                           .add("/add/object", Json.createObjectBuilder()
 *                                                   .add("foo", "bar")
 *                                                   .build())
 *                           .remove("/remove/it")
 *                           .move("/move/to", "move/from")
 *                           .build();
 *
 * </pre>
 * <p>
 * An instance of a JsonPatchBuilder can be reused for another {@link JsonPatch} after
 * the {@link #build()}-Method was called.
 */
public interface JsonPatchBuilder {

    /**
     * Adds an 'add'-operation to the {@link JsonPatch}
     *
     * @param path as {@link JsonPointer} where the value should be added
     * @param value the value to add
     *
     * @return the builder instance for chained method calls
     *
     * @throws NullPointerException if the given {@code path} is {@code null}
     */
    JsonPatchBuilder add(String path, JsonValue value);

    /**
     * @see #add(String, JsonValue)
     */
    JsonPatchBuilder add(String path, String value);

    /**
     * @see #add(String, JsonValue)
     */
    JsonPatchBuilder add(String path, int value);

    /**
     * @see #add(String, JsonValue)
     */
    JsonPatchBuilder add(String path, boolean value);


    /**
     * Adds a 'remove'-operation to the {@link JsonPatch}
     *
     * @param path as {@link JsonPointer} of the value which should get removed
     *
     * @return the builder instance for chained method calls
     *
     * @throws NullPointerException if the given {@code path} is {@code null}
     */
    JsonPatchBuilder remove(String path);


    /**
     * Adds a 'replace'-operation to the {@link JsonPatch}
     *
     * @param path as {@link JsonPointer} to the value which should get replaced
     * @param value the new value
     *
     * @return the builder instance for chained method calls
     *
     * @throws NullPointerException if the given {@code path} is {@code null}
     */
    JsonPatchBuilder replace(String path, JsonValue value);

    /**
     * @see #replace(String, JsonValue)
     */
    JsonPatchBuilder replace(String path, String value);

    /**
     * @see #replace(String, JsonValue)
     */
    JsonPatchBuilder replace(String path, int value);

    /**
     * @see #replace(String, JsonValue)
     */
    JsonPatchBuilder replace(String path, boolean value);


    /**
     * Adds a 'move'-operation to the {@link JsonPatch}
     *
     * @param path where the value should get inserted as {@link JsonPointer}
     * @param from where the value should be taken from as {@link JsonPointer}
     *
     * @return the builder instance for chained method calls
     *
     * @throws NullPointerException if the given {@code path} is {@code null}
     *                              if the given {@code from} is {@code null}
     */
    JsonPatchBuilder move(String path, String from);


    /**
     * Adds a 'copy'-operation to the {@link JsonPatch}
     *
     * @param path where the copied value should get inserted as {@link JsonPointer}
     * @param from value to copy as {@link JsonPointer}
     *
     * @return the builder instance for chained method calls
     *
     * @throws NullPointerException if the given {@code path} is {@code null}
     *                              if the given {@code from} is {@code null}
     */
    JsonPatchBuilder copy(String path, String from);


    /**
     * Adds a 'test'-operation to the {@link JsonPointer}
     *
     * @param path as {@link JsonPointer} to the value to test
     * @param value value to test

     * @return the builder instance for chained method calls

     * @throws NullPointerException if the given {@code path} is {@code null}
     */
    JsonPatchBuilder test(String path, JsonValue value);

    /**
     * @see #test(String, JsonValue)
     */
    JsonPatchBuilder test(String path, String value);

    /**
     * @see #test(String, JsonValue)
     */
    JsonPatchBuilder test(String path, int value);

    /**
     * @see #test(String, JsonValue)
     */
    JsonPatchBuilder test(String path, boolean value);


    JsonPatch build();
}

