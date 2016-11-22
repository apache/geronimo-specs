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
 * <p>This class is an immutable representation of a JSON Pointer as specified in
 * <a href="http://tools.ietf.org/html/rfc6901">RFC 6901</a>.
 * </p>
 *
 * <p>JSON Pointer is a string syntax for identifying a specific value
 * within a JavaScript Object Notation (JSON) document [RFC4627].
 * JSON Pointer is intended to be easily expressed in JSON string values
 * as well as Uniform Resource Identifier (URI) [RFC3986] fragment identifiers.
 * </p>
 * <p> The method {@link #getValue getValue()} returns the referenced value.
 * The methods {@link #add add()}, {@link #replace replace()},
 * and {@link #remove remove()} executes the operations specified in
 * <a href="http://tools.ietf.org/html/rfc6902">RFC 6902</a>. </p>
 *
 * @since 1.1
 */
public interface JsonPointer {

    JsonValue getValue(JsonStructure target);

    JsonStructure add(JsonStructure target, JsonValue value);

    JsonStructure replace(JsonStructure target, JsonValue value);

    JsonStructure remove(JsonStructure target);

    JsonObject add(JsonObject target, JsonValue value);

    JsonArray add(JsonArray target, JsonValue value);

    JsonObject replace(JsonObject target, JsonValue value);

    JsonArray replace(JsonArray target, JsonValue value);

    JsonObject remove(JsonObject target);

    JsonArray remove(JsonArray target);
}
