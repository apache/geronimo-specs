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
package jakarta.json;

/**
 * A single value in a JSON expression.
 */
public interface JsonValue {

    /**
     * The empty JSON object.
     */
    JsonObject EMPTY_JSON_OBJECT = new EmptyJsonObject();

    /**
     * The empty JSON array.
     */
    JsonArray EMPTY_JSON_ARRAY = new EmptyJsonArray();


    /**
     * A constant JsonValue for null values
     */
    JsonValue NULL = new JsonValue() {
        @Override
        public ValueType getValueType() {
            return ValueType.NULL;
        }

        @Override
        public boolean equals(final Object obj) {
            return obj instanceof JsonValue && getValueType().equals(JsonValue.class.cast(obj).getValueType());
        }

        @Override
        public int hashCode() {
            return ValueType.NULL.hashCode();
        }

        @Override
        public String toString() {
            return "null";
        }
    };

    /**
     * A constant JsonValue for TRUE
     */
    JsonValue TRUE = new JsonValue() {
        @Override
        public ValueType getValueType() {
            return ValueType.TRUE;
        }

        @Override
        public boolean equals(final Object obj) {
            return obj instanceof JsonValue && getValueType().equals(JsonValue.class.cast(obj).getValueType());
        }

        @Override
        public int hashCode() {
            return ValueType.TRUE.hashCode();
        }

        @Override
        public String toString() {
            return Boolean.TRUE.toString();
        }
    };

    /**
     * A constant JsonValue for FALSE
     */
    JsonValue FALSE = new JsonValue() {
        @Override
        public ValueType getValueType() {
            return ValueType.FALSE;
        }

        @Override
        public boolean equals(final Object obj) {
            return obj instanceof JsonValue && getValueType().equals(JsonValue.class.cast(obj).getValueType());
        }

        @Override
        public int hashCode() {
            return ValueType.FALSE.hashCode();
        }

        @Override
        public String toString() {
            return Boolean.FALSE.toString();
        }
    };

    ValueType getValueType();

    @Override
    String toString();

    enum ValueType {
        ARRAY,
        OBJECT, STRING, NUMBER,
        TRUE, FALSE,
        NULL
    }
    
    default JsonObject asJsonObject() {
        return JsonObject.class.cast(this);
    }

    default JsonArray asJsonArray() {
        return JsonArray.class.cast(this);
    }

}
