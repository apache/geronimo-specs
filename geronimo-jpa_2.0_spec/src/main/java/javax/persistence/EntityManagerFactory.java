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

import java.util.Map;
import java.util.Set;

/**
 * Interface used to interact with the entity manager factory
 * for the persistence unit. 
 * @version $Rev$ $Date$
 */
public interface EntityManagerFactory {

    /**
    * Create a new EntityManager.
    * This method returns a new EntityManager instance each time
    * it is invoked.
    * The isOpen method will return true on the returned instance.
    * @throws IllegalStateException if the entity manager factory
    * has been closed.
    */
    public EntityManager createEntityManager();
    /**
    * Create a new EntityManager with the specified Map of
    * properties.
    * This method returns a new EntityManager instance each time
    * it is invoked.
    * The isOpen method will return true on the returned instance.
    * @throws IllegalStateException if the entity manager factory
    * has been closed.
    */
    public EntityManager createEntityManager(Map map);
    /**
    * Return an instance of QueryBuilder for the creation of
    * Criteria API QueryDefinition objects.
    * @return QueryBuilder instance
    * @throws IllegalStateException if the entity manager factory
    * has been closed.
    */
    public QueryBuilder getQueryBuilder();
    /**
    * Close the factory, releasing any resources that it holds.
    * After a factory instance is closed, all methods invoked on
    * it will throw an IllegalStateException, except for isOpen,
    * which will return false. Once an EntityManagerFactory has
    * been closed, all its entity managers are considered to be
    * in the closed state.
    * @throws IllegalStateException if the entity manager factory
    * has been closed.
    */
    public void close();
    /**
    * Indicates whether the factory is open. Returns true
    * until the factory has been closed.
    */
    public boolean isOpen();
    /**
    * Get the properties and associated values that are in effect
    * for the entity manager factory. Changing the contents of the
    * map does not change the configuration in effect.
    * @return properties
    */
    public Map getProperties();
    /**
    * Get the names of the properties that are supported for use
    * with the entity manager factory. These correspond to
    * properties that may be passed to the methods of the
    * EntityManagerFactory interface that take a properties
    * argument. These include all standard properties as well as
    * vendor-specific properties supported by the provider. These
    * properties may or may not currently be in effect.
    * @return properties and hints
    */
    public Set<String> getSupportedProperties();
    /**
    * Access the cache that is associated with the entity manager
    * factory (the "second level cache").
    * @return instance of the Cache interface
    * @throws IllegalStateException if the entity manager factory
    * has been closed.
    */
    public Cache getCache();
}
