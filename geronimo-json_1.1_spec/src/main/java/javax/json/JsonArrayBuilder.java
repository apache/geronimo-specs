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

public interface JsonArrayBuilder {
    JsonArrayBuilder add(JsonValue value);

    JsonArrayBuilder add(String value);

    JsonArrayBuilder add(BigDecimal value);

    JsonArrayBuilder add(BigInteger value);

    JsonArrayBuilder add(int value);

    JsonArrayBuilder add(long value);

    JsonArrayBuilder add(double value);

    JsonArrayBuilder add(boolean value);

    JsonArrayBuilder addNull();

    JsonArrayBuilder add(JsonObjectBuilder builder);

    JsonArrayBuilder add(JsonArrayBuilder builder);

    JsonArray build();

    JsonArrayBuilder addAll(JsonArrayBuilder builder);

    JsonArrayBuilder add(int index, JsonValue value);

    JsonArrayBuilder add(int index, String value);

    JsonArrayBuilder add(int index, BigDecimal value);

    JsonArrayBuilder add(int index, BigInteger value);

    JsonArrayBuilder add(int index, int value);

    JsonArrayBuilder add(int index, long value);

    JsonArrayBuilder add(int index, double value);

    JsonArrayBuilder add(int index, boolean value);

    JsonArrayBuilder addNull(int index);

    JsonArrayBuilder add(int index, JsonObjectBuilder builder);

    JsonArrayBuilder add(int index, JsonArrayBuilder builder);

    JsonArrayBuilder set(int index, JsonValue value);

    JsonArrayBuilder set(int index, String value);

    JsonArrayBuilder set(int index, BigDecimal value);

    JsonArrayBuilder set(int index, BigInteger value);

    JsonArrayBuilder set(int index, int value);

    JsonArrayBuilder set(int index, long value);

    JsonArrayBuilder set(int index, double value);

    JsonArrayBuilder set(int index, boolean value);

    JsonArrayBuilder setNull(int index);

    JsonArrayBuilder set(int index, JsonObjectBuilder builder);

    JsonArrayBuilder set(int index, JsonArrayBuilder builder);

    JsonArrayBuilder remove(int index);
}
