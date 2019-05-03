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
 *
 *
 * <p>JSON Pointer is a string syntax for identifying a specific value
 * within a JavaScript Object Notation (JSON) document [RFC4627].
 * JSON Pointer is intended to be easily expressed in JSON string values
 * as well as Uniform Resource Identifier (URI) [RFC3986] fragment identifiers.
 *
 * <p> The method {@link #getValue getValue()} returns the referenced value.
 * The methods {@link #add add()}, {@link #replace replace()},
 * and {@link #remove remove()} executes the operations specified in
 * <a href="http://tools.ietf.org/html/rfc6902">RFC 6902</a>.
 *
 * @since 1.1
 */
public interface JsonPointer {

    /**
     * Get the JsonValue at the position referenced by this JsonPointer.
     * @param target the JSON to apply this JsonPointer on
     * @return the
     * @throws JsonException if no value exists at the referenced location
     * @throws NullPointerException if the target is {@code null}
     */
    JsonValue getValue(JsonStructure target);


    /**
     * Add or replace the value at the position referenced by this JsonPointer with
     * the new value
     * @param target structure in which the newValue should be added or replaced
     * @param newValue the new value to set
     * @param <T>
     * @return the new structure after modifying the original JsonStrucure
     * @throws JsonException if no value exists at the referenced location
     * @throws NullPointerException if the target is {@code null}
     */
    <T extends JsonStructure> T add(T target, JsonValue newValue);


    /**
     * Remove the value from the referenced position inside the target JSON.
     * @param target to remove the value from
     * @param <T> the type of the passed JsonStructure
     * @return a new JsonStructure with the value removed from the target
     * @throws JsonException if no value exists at the referenced location
     * @throws NullPointerException if the target is {@code null}
     */
    <T extends JsonStructure> T remove(T target);

    /**
     * Replace the value at the position referenced by this JsonPointer with
     * the newValue.
     *
     * @param target structure in which the newValue should be replaced
     * @param newValue the new value to set
     * @param <T>
     * @return the new structure after modifying the original JsonStrucure
     * @throws JsonException if no value exists at the referenced location
     * @throws NullPointerException if the target is {@code null}
     */
    <T extends JsonStructure> T  replace(T target, JsonValue newValue);

    /**
     * Verify if the target JSON structure contains a value at the referenced location.
     * @param target to check
     * @return {@code true} if there is a value at the referenced location, {@code false} otherwise
     * @throws NullPointerException if the target is {@code null}
     */
    boolean containsValue(JsonStructure target);

}
