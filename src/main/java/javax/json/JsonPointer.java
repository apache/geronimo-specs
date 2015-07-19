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

public final class JsonPointer {

    public JsonPointer(String jsonPointer) {
        
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public JsonValue getValue(JsonStructure target) {
        return null;
    }

    public JsonStructure add(JsonStructure target, JsonValue value) {
        return null;
    }

    public JsonStructure replace(JsonStructure target, JsonValue value) {
        return null;
    }

    public JsonStructure remove(JsonStructure target) {
        return null;
    }

    public JsonObject add(JsonObject target, JsonValue value) {
        return (JsonObject) add((JsonStructure) target, value);
    }

    public JsonArray add(JsonArray target, JsonValue value) {
        return (JsonArray) add((JsonStructure) target, value);
    }

    public JsonObject replace(JsonObject target, JsonValue value) {
        return (JsonObject) replace((JsonStructure) target, value);
    }

    public JsonArray replace(JsonArray target, JsonValue value) {
        return (JsonArray) replace((JsonStructure) target, value);
    }

    public JsonObject remove(JsonObject target) {
        return (JsonObject) remove((JsonStructure) target);
    }

    public JsonArray remove(JsonArray target) {
        return (JsonArray) remove((JsonStructure) target);
    }
}
