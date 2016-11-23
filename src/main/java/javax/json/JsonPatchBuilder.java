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
public interface JsonPatchBuilder {



    JsonStructure apply(JsonStructure target);

    JsonObject apply(JsonObject target);
    
    JsonArray apply(JsonArray target);
    
    JsonPatchBuilder add(String path, JsonValue value);

    JsonPatchBuilder add(String path, String value);

    JsonPatchBuilder add(String path, int value);

    JsonPatchBuilder add(String path, boolean value);

    JsonPatchBuilder remove(String path);

    JsonPatchBuilder replace(String path, JsonValue value);

    JsonPatchBuilder replace(String path, String value);

    JsonPatchBuilder replace(String path, int value);

    JsonPatchBuilder replace(String path, boolean value);

    JsonPatchBuilder move(String path, String from);
 
    JsonPatchBuilder copy(String path, String from);
 

    JsonPatchBuilder test(String path, JsonValue value);

    JsonPatchBuilder test(String path, String value);

    JsonPatchBuilder test(String path, int value);

    JsonPatchBuilder test(String path, boolean value);

    JsonArray build();
}

