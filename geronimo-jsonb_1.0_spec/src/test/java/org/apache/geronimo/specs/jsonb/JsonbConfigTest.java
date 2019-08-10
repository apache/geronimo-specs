/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.geronimo.specs.jsonb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.json.bind.JsonbConfig;
import javax.json.bind.adapter.JsonbAdapter;

import org.junit.jupiter.api.Test;

class JsonbConfigTest {
    @Test
    void accumulate() {
        final JsonbConfig jsonbConfig = new JsonbConfig()
                .withAdapters(new PassthroughIntegerAdapter())
                .withAdapters(new PassthroughStringAdapter());

        final Object adapters = jsonbConfig.getProperty(JsonbConfig.ADAPTERS).orElseThrow(AssertionError::new);
        assertTrue(JsonbAdapter[].class.isAssignableFrom(adapters.getClass()));
        assertEquals(2, JsonbAdapter[].class.cast(adapters).length);
    }

    public static class PassthroughStringAdapter implements JsonbAdapter<String, String> {
        @Override
        public String adaptToJson(final String obj) {
            return obj;
        }

        @Override
        public String adaptFromJson(final String obj) {
            return obj;
        }
    }

    public static class PassthroughIntegerAdapter implements JsonbAdapter<Integer, Integer> {
        @Override
        public Integer adaptToJson(final Integer obj) {
            return obj;
        }

        @Override
        public Integer adaptFromJson(final Integer obj) {
            return obj;
        }
    }
}
