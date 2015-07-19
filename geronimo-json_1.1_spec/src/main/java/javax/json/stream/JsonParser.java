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

import java.io.Closeable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Stream;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

public interface JsonParser extends Closeable {
    boolean hasNext();

    Event next();

    String getString();

    boolean isIntegralNumber();

    int getInt();

    long getLong();

    BigDecimal getBigDecimal();

    JsonLocation getLocation();

    @Override
    void close();

    enum Event {
        START_ARRAY, START_OBJECT,
        KEY_NAME,
        VALUE_STRING, VALUE_NUMBER, VALUE_TRUE, VALUE_FALSE, VALUE_NULL,
        END_OBJECT, END_ARRAY
    }
    
    default public JsonObject getObject() {
        throw new UnsupportedOperationException();
    }

    default public JsonValue getValue() {
        throw new UnsupportedOperationException();
    }

    default public JsonArray getArray() {
        throw new UnsupportedOperationException();
    }

    default public Stream<JsonValue> getArrayStream() {
        throw new UnsupportedOperationException();
    }

    default public Stream<Map.Entry<String,JsonValue>> getObjectStream() {
        throw new UnsupportedOperationException();
    }

    default public Stream<JsonValue> getValueStream() {
        throw new UnsupportedOperationException();
    }

    default public void skipArray() {
        throw new UnsupportedOperationException();
    }

    default public void skipObject() {
        throw new UnsupportedOperationException();
    }
}

