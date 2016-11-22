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

/**
 * TODO this is a final class in the spec, but that makes no sense.
 * Use {@link JsonProvider#createPatchBuilder()}
 * A JsonPatchBuilder contains state and should not get used concurrently.
 */
public abstract class JsonPatchBuilder {

    public JsonPatchBuilder() {

    }

    public JsonStructure apply(JsonStructure target) {
        return new JsonPatch(build()).apply(target);
    }

    public JsonObject apply(JsonObject target) {
        return new JsonPatch(build()).apply(target);
    }

    public JsonArray apply(JsonArray target) {
        return new JsonPatch(build()).apply(target);
    }

    public JsonPatchBuilder add(String path, JsonValue value) {
        return null;
    }

    public JsonPatchBuilder add(String path, String value) {
        return null;
    }

    public JsonPatchBuilder add(String path, int value) {
        return null;
    }

    public JsonPatchBuilder add(String path, boolean value) {
        return null;
    }

    public JsonPatchBuilder remove(String path) {
        return null;
    }

    public JsonPatchBuilder replace(String path, JsonValue value) {
        return null;
    }

    public JsonPatchBuilder replace(String path, String value) {
        return null;
    }

    public JsonPatchBuilder replace(String path, int value) {
        return null;
    }

    public JsonPatchBuilder replace(String path, boolean value) {
        return null;
    }

    public JsonPatchBuilder move(String path, String from) {
        return null;
    }
 
    public JsonPatchBuilder copy(String path, String from) {
        return null;
    }
 

    public JsonPatchBuilder test(String path, JsonValue value) {
        return null;
    }

    public JsonPatchBuilder test(String path, String value) {
        return null;
    }

    public JsonPatchBuilder test(String path, int value) {
        return null;
    }

    public JsonPatchBuilder test(String path, boolean value) {
        return null;
    }

    public JsonArray build() {
        return null;
    }
}

