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

import java.util.Map;

public interface JsonBuilderFactory {
    /**
     * @return a new empty JsonObjectBuilder
     */
    JsonObjectBuilder createObjectBuilder();

    /**
     * @return a new empty JsonArrayBuilder
     */
    JsonArrayBuilder createArrayBuilder();

    /**
     * @return the config which got used when creating this builder factory.
     */
    Map<String, ?> getConfigInUse();

    /**
     * Create a JsonObjectBuilder filled with the given initial data.
     *
     * @throws NullPointerException if initialData is {@code null}
     *
     * @return a new pre initialised JsonObjectBuilder
     *
     * @since 1.1
     */
    default JsonObjectBuilder createObjectBuilder(JsonObject initialData) {
        throw new UnsupportedOperationException();
    }

    /**
     * Create a JsonObjectBuilder filled with the given initial data.
     *
     * @throws NullPointerException if initialData is {@code null}
     *
     * @return a new pre initialised JsonObjectBuilder
     *
     * @since 1.1
     */
    default JsonObjectBuilder createObjectBuilder(Map<String, Object> initialData) {
        throw new UnsupportedOperationException();
    }

    /**
     * Create a JsonArrayBuilder filled with the given initial data.
     *
     * @throws NullPointerException if initialData is {@code null}
     *
     * @return a new pre initialised JsonArrayBuilder
     *
     * @since 1.1
     */
    default JsonArrayBuilder createArrayBuilder(JsonArray initialData) {
        throw new UnsupportedOperationException();
    }

    /**
     * Create a {@link JsonArrayBuilder} which is filled with the given initial content.
     * @param initialData the content to immediately add to the JsonArrayBuilder
     *
     * @throws NullPointerException if initialData is {@code null}
     *
     * @return a new pre initialised JsonArrayBuilder
     * @since 1.1
     */
    default JsonArrayBuilder createArrayBuilder(java.util.Collection<?> initialData) {
        throw new UnsupportedOperationException();
    }
}

