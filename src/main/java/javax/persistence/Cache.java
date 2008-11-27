/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

//
// This source code implements specifications defined by the Java
// Community Process. In order to remain compliant with the specification
// DO NOT add / change / or delete method signatures!
//
package javax.persistence;

/**
 * Interface used to interact with the second-level cache.
 * If a cache is not in use, the methods of this interface have
 * no effect, except for contains, which returns false.
 */
public interface Cache {
    /**
     * Whether the cache contains data for the given entity.
     */
    public boolean contains(Class cls, Object primaryKey);

    /**
     * Remove the data for the given entity from the cache.
     */
    public void evict(Class cls, Object primaryKey);

    /**
     * Remove the data for entities of the specified class (and its
     * subclasses) from the cache.
     */
    public void evict(Class cls);

    /**
     * Clear the cache.
     */
    public void evictAll();
}
