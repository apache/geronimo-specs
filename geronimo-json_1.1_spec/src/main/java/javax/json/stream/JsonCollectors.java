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

package javax.json.stream;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

public final class JsonCollectors {

    private JsonCollectors() {
    }

    public static Collector<JsonValue, JsonObjectBuilder, JsonObject>
                toJsonObject(Function<JsonValue, String> keyMapper,
                             Function<JsonValue, JsonValue> valueMapper) {
        return null;
    }

    public static Collector<JsonValue, Map<String, JsonArrayBuilder>, JsonObject>
                groupingBy(Function<JsonValue, String> classifier,
                           Collector<JsonValue, JsonArrayBuilder, JsonArray> downstream) {

        return null;
    }

    public static Collector<JsonValue, Map<String, JsonArrayBuilder>, JsonObject>
                groupingBy(Function<JsonValue, String> classifier) {
        return null;
    }
}

