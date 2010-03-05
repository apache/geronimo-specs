/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.osgi.registry.api;

import java.util.List;

/**
 * The implementation of the factory registry used to store
 * the bundle registrations.
 */
public interface ProviderRegistry {
    /**
     * Locate a class by its factory id indicator. .
     *
     * @param factoryId The factory id (generally, a fully qualified class name).
     *
     * @return The Class corresponding to this factory id.  Returns null
     *         if this is not registered or the indicated class can't be
     *         loaded.
     */
    public Class<?> locate(String factoryId);

    /**
     * Locate all class files that match a given factory id.
     *
     * @param factoryId The target factory identifier.
     *
     * @return A List containing the class objects corresponding to the
     *         factory identifier.  Returns an empty list if no
     *         matching classes can be located.
     */
    public List<Class<?>> locateAll(String factoryId);
}
