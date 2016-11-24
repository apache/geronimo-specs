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

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A JsonObjectBuilder can be used to build {@link JsonObject JsonObjects}.
 * Instances are not thread safe.
 */
public interface JsonObjectBuilder {
    JsonObjectBuilder add(String name, JsonValue value);

    JsonObjectBuilder add(String name, String value);

    JsonObjectBuilder add(String name, BigInteger value);

    JsonObjectBuilder add(String name, BigDecimal value);

    JsonObjectBuilder add(String name, int value);

    JsonObjectBuilder add(String name, long value);

    JsonObjectBuilder add(String name, double value);

    JsonObjectBuilder add(String name, boolean value);

    JsonObjectBuilder addNull(String name);

    JsonObjectBuilder add(String name, JsonObjectBuilder builder);

    JsonObjectBuilder add(String name, JsonArrayBuilder builder);

    JsonObject build();

    JsonObjectBuilder addAll(JsonObjectBuilder builder);

    JsonObjectBuilder remove(String name);
}
