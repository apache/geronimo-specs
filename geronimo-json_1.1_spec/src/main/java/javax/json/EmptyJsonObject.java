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

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Set;

class EmptyJsonObject extends AbstractMap<String, JsonValue> implements JsonObject, Serializable {
    @Override
    public Set<Entry<String, JsonValue>> entrySet() {
        return Collections.emptySet();
    }

    @Override
    public JsonArray getJsonArray(String name) {
        return null;
    }

    @Override
    public JsonObject getJsonObject(String name) {
        return null;
    }

    @Override
    public JsonNumber getJsonNumber(String name) {
        return null;
    }

    @Override
    public JsonString getJsonString(String name) {
        return null;
    }

    @Override
    public String getString(String name) {
        throw new NullPointerException("Calling getString on EmptyJsonObject");
    }

    @Override
    public String getString(String name, String defaultValue) {
        return defaultValue;
    }

    @Override
    public int getInt(String name) {
        throw new NullPointerException("Calling getInt on EmptyJsonObject");
    }

    @Override
    public int getInt(String name, int defaultValue) {
        return defaultValue;
    }

    @Override
    public boolean getBoolean(String name) {
        throw new NullPointerException("Calling getInt on EmptyJsonObject");
    }

    @Override
    public boolean getBoolean(String name, boolean defaultValue) {
        return defaultValue;
    }

    @Override
    public boolean isNull(String name) {
        throw new NullPointerException("Calling isNull on EmptyJsonObject");
    }

    @Override
    public ValueType getValueType() {
        return ValueType.OBJECT;
    }
}
