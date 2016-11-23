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
 * JsonPatch (RFC6902 https://tools.ietf.org/html/rfc6902)
 * is a format for expressing a sequence of operations to apply to
 * a target JSON document; it is suitable for use with the HTTP PATCH
 * method.
 *
 * A JsonPatch is an array of 'operations' in the form e.g.
 *
 * <pre>
 * [
 * { "op": "add", "path": "/foo/-", "value": ["abc", "def"] }
 * { "path": "/a/b/c", "op": "add", "value": "foo" }
 * ]
 * </pre>
 *
 * @since 1.1
 *
 */
public interface JsonPatch {

    JsonStructure apply(JsonStructure target);

    JsonObject apply(JsonObject target);

    JsonArray apply(JsonArray target);

    public static JsonArray diff(JsonStructure source, JsonStructure target) {
        return null;
    }
}

