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

import java.util.Set;
import java.util.Map;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;


public interface EntityManager {

    public void persist(Object entity);
    public <T> T merge(T entity);
    public void remove(Object entity);
    public <T> T find(Class<T> entityClass, Object primaryKey);
    public <T> T find(Class<T> entityClass, Object primaryKey, 
                      Map<String, Object> properties); 
    public <T> T find(Class<T> entityClass, Object primaryKey,
                      LockModeType lockMode);

    public <T> T find(Class<T> entityClass, Object primaryKey,
                      LockModeType lockMode, 
                      Map<String, Object> properties);

    public <T> T getReference(Class<T> entityClass, 
                                  Object primaryKey);

    public void flush();

    public void setFlushMode(FlushModeType flushMode);

    public FlushModeType getFlushMode();

    public void lock(Object entity, LockModeType lockMode);

    public void lock(Object entity, LockModeType lockMode,
                     Map<String, Object> properties);

    public void refresh(Object entity);

    public void refresh(Object entity,
                            Map<String, Object> properties); 

    public void refresh(Object entity, LockModeType lockMode);

    public void refresh(Object entity, LockModeType lockMode,
                        Map<String, Object> properties);
    public void clear();

    public void detach(Object entity); 

    public boolean contains(Object entity);

    public LockModeType getLockMode(Object entity);

    public void setProperty(String propertyName, Object value);

    public Map<String, Object> getProperties();

    public Query createQuery(String qlString);

    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery); 

    public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass);

    public Query createNamedQuery(String name);

    public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass);

    public Query createNativeQuery(String sqlString);

    public Query createNativeQuery(String sqlString, Class resultClass);

    public Query createNativeQuery(String sqlString, String resultSetMapping);

    public void joinTransaction();

    public <T> T unwrap(Class<T> cls); 

    public Object getDelegate();

    public void close();

    public boolean isOpen();

    public EntityTransaction getTransaction();

    public EntityManagerFactory getEntityManagerFactory();

    public CriteriaBuilder getCriteriaBuilder();

    public Metamodel getMetamodel();
}
