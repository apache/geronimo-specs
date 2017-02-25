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
 * <p>
 * JsonPatch is a format for expressing a sequence of operations to apply on
 * a target JSON document.
 * </p>
 * <p>
 * A JsonPatch is an array of 'operations' in the form e.g.
 * <p>
 * <pre>
 * [
 *   { "op": "add", "path": "/foo/-", "value": ["abc", "def"] }
 *   { "path": "/a/b/c", "op": "add", "value": "foo" }
 * ]
 * </pre>
 * <p>
 * The 'operations' are performed in the order they are in the JsonPatch and applied
 * to the 'result' JSON document from the previous operation.
 * <p>
 *
 * @see Operation
 *
 * @since 1.1
 */
public interface JsonPatch {

    /**
     * <p>
     * An enumeration of available operations for {@link JsonPatch}.<br>
     * <p>
     * NOTICE: the behavoir of some operations depends on which {@link JsonValue} they are performed.
     * for details please check the documentation of the very operation.
     *
     * @see <a href="http://tools.ietf.org/html/rfc6902">RFC-6902</a>
     *
     * @since 1.1
     */
    enum Operation {

        /**
         * <p>
         * required members are:
         * <ul>
         * <li>"op": "add"</li>
         * <li>"path": "path/to/add"</li>
         * <li>"value": "{@link JsonValue}ToAdd"</li>
         * </ul>
         * <p>
         * if the "path/to" does not exist, this operation will result in an error.<br>
         * if the "path/to/add" already exists, the value will be <strong>replaced</strong>
         * <p>
         * for {@link JsonArray}s the new value will be inserted at the specified index
         * and the element(s) at/after are shifted to the right. the '-' character is used to append the value
         * at the and of the {@link JsonArray}.
         */
        ADD("add"),

        /**
         * <p>
         * required members are:
         * <ul>
         * <li>"op": "remove"</li>
         * <li>"path": "path/to/remove"</li>
         * </ul>
         * <p>
         * if the "path/to/remove" does not exist, the operation will fail.
         * <p>
         * for {@link JsonArray}s the values after the removed value are shifted to the left
         */
        REMOVE("remove"),

        /**
         * <p>
         * required members are:
         * <ul>
         * <li>"op": "replace"</li>
         * <li>"path": "path/to/replace"</li>
         * <li>"value": "the new {@link JsonValue}"</li>
         * </ul>
         * <p>
         * this operation is identical to {@link #REMOVE} followed by {@link #ADD}
         */
        REPLACE("replace"),

        /**
         * <p>
         * required members are:
         * <ul>
         * <li>"op": "move"</li>
         * <li>"from": "path/to/move/from"</li>
         * <li>"path": "path/to/move/to"</li>
         * </ul>
         * <p>
         * the operation will fail it the "path/to/move/from" does not exist
         * <p>
         * NOTICE: a location can not be moved into one of it's children. (from /a/b/c to /a/b/c/d)
         * <p>
         * this operation is identical to {@link #REMOVE} from "from" and {@link #ADD} to the "path"
         */
        MOVE("move"),

        /**
         * <p>
         * required members are:
         * <ul>
         * <li>"op": "copy"</li>
         * <li>"from": "path/to/copy/from"</li>
         * <li>"path": "path/to/add"</li>
         * </ul>
         * <p>
         * the operation will result in an error if the "from" location does not exist
         * <p>
         * this operation is identical to {@link #ADD} with the "from" value
         */
        COPY("copy"),

        /**
         * <p>
         * required members are:
         * <ul>
         * <li>"op": "test"</li>
         * <li>"path": "/path/to/test"</li>
         * <li>"value": "{@link JsonValue} to test"</li>
         * </ul>
         * <p>
         * this operation fails, if the value is NOT equal with the /path/to/test
         * <p>
         * ordering of the elements in a {@link JsonObject} is NOT significant however
         * the position of an element in a {@link JsonArray} is significant for equality.
         */
        TEST("test");


        private final String operationName;


        Operation(String operationName) {
            this.operationName = operationName;
        }


        /**
         * @return the JSON operation name
         */
        public String operationName() {
            return operationName;
        }

        /**
         * Returns the {@link Operation} for the given {@code operationName}. If no {@link Operation}
         * is present, a {@link JsonException} will be thrown.
         *
         * @param operationName {@code operationName} to convert to the enum constant.
         *
         * @return the {@link Operation Operation-constant} for given {@code operationName}
         *
         * @throws JsonException if no {@link Operation} has been found for the given {@code operationName}
         */
        public static Operation fromOperationName(String operationName) {

            for (Operation op : values()) {
                if (op.operationName().equalsIgnoreCase(operationName)) {
                    return op;
                }
            }

            throw new JsonException("unknown value for the operationName of the JSON patch operation: " + operationName);
        }
    }


    /**
     * Applies the {@link JsonPatch} to the given {@code target}. If the
     * given {@code target} is {@code null} a {@link NullPointerException}
     * will be thrown.
     *
     * @param target - the target to apply the {@link JsonPatch}
     *
     * @return 'patched' {@link JsonStructure}
     *
     * @throws NullPointerException if {@code target} is {@code null}
     */
    <T extends JsonStructure> T apply(T target);

    /**
     * @return the JsonPatch as {@link JsonArray}
     */
    JsonArray toJsonArray();
}

