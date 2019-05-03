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

import java.io.Serializable;
import java.util.AbstractList;
import java.util.List;

class EmptyJsonArray extends AbstractList<JsonValue> implements JsonArray, Serializable {
    @Override
    public JsonValue get(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public JsonObject getJsonObject(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public JsonArray getJsonArray(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public JsonNumber getJsonNumber(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public JsonString getJsonString(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public <T extends JsonValue> List<T> getValuesAs(Class<T> clazz) {
        return null;
    }

    @Override
    public String getString(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public String getString(int index, String defaultValue) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int getInt(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int getInt(int index, int defaultValue) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public boolean getBoolean(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public boolean getBoolean(int index, boolean defaultValue) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public boolean isNull(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public ValueType getValueType() {
        return ValueType.ARRAY;
    }
}
