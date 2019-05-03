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

package jakarta.json.stream;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;

public final class JsonCollectors {

    private JsonCollectors() {
        // no instantiation for utility classes
    }


    public static Collector<JsonValue, JsonArrayBuilder, JsonArray> toJsonArray() {

        return Collector.of(Json::createArrayBuilder,
                            JsonArrayBuilder::add,
                            JsonArrayBuilder::addAll,
                            JsonArrayBuilder::build);
    }

    public static Collector<Map.Entry<String, JsonValue>, JsonObjectBuilder, JsonObject> toJsonObject() {

        return Collector.of(Json::createObjectBuilder,
                            JsonCollectors::addEntry,
                            JsonObjectBuilder::addAll,
                            JsonObjectBuilder::build);
    }

    public static Collector<JsonValue, JsonObjectBuilder, JsonObject> toJsonObject(Function<JsonValue, String> keyMapper,
                                                                                   Function<JsonValue, JsonValue> valueMapper) {

        return Collector.of(Json::createObjectBuilder,
                            (b, v) -> b.add(keyMapper.apply(v), valueMapper.apply(v)),
                            JsonObjectBuilder::addAll,
                            JsonObjectBuilder::build);
    }

    public static <T extends JsonArrayBuilder> Collector<JsonValue, Map<String, T>, JsonObject> groupingBy(Function<JsonValue, String> classifier,
                                                                                                           Collector<JsonValue, T, JsonArray> downstream) {
        return Collector.of(HashMap::new,
                            (map, value) -> accumulator(map, value, classifier, downstream),
                            JsonCollectors::combiner,
                            m -> finisher(m, downstream.finisher()));
    }

    public static Collector<JsonValue, Map<String, JsonArrayBuilder>, JsonObject> groupingBy(Function<JsonValue, String> classifier) {
        return groupingBy(classifier, toJsonArray());
    }


    private static void addEntry(JsonObjectBuilder objectBuilder, Map.Entry<String, JsonValue> entry) {
        objectBuilder.add(entry.getKey(), entry.getValue());
    }

    private static <T extends JsonArrayBuilder> void accumulator(Map<String, T> map,
                                                                 JsonValue value,
                                                                 Function<JsonValue, String> classifier,
                                                                 Collector<JsonValue, T, JsonArray> downstream) {

        String key = classifier.apply(value);
        T arrayBuilder = map.computeIfAbsent(key, k -> downstream.supplier().get());
        downstream.accumulator().accept(arrayBuilder, value);
    }

    private static <T extends JsonArrayBuilder> Map<String, T> combiner(Map<String, T> target, Map<String, T> source) {
        target.putAll(source);
        return target;
    }

    private static <T extends JsonArrayBuilder> JsonObject finisher(Map<String, T> arrayBuilders,
                                                                    Function<T, JsonArray> downstream) {

        JsonObjectBuilder builder = Json.createObjectBuilder();
        arrayBuilders.forEach((key, value) -> builder.add(key, downstream.apply(value)));

        return builder.build();
    }
}

