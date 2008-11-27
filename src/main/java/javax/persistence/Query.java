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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface used to control query execution.
 *
 * @version $Rev$ $Date$
 */
public interface Query {

    /**
     * Execute a SELECT query and return the query results
     * as a List.
     *
     * @return a list of the results
     * @throws IllegalStateException        if called for a Java
     *                                      Persistence query language UPDATE or DELETE statement
     * @throws QueryTimeoutException        if the query execution exceeds
     *                                      the query timeout value set
     * @throws TransactionRequiredException if a lock mode has
     *                                      been set and there is no transaction
     * @throws PessimisticLockException     if pessimistic locking
     *                                      fails and the transaction is rolled back
     * @throws LockTimeoutException         if pessimistic locking
     *                                      fails and only the statement is rolled back
     */
    public List getResultList();

    /**
     * Execute a SELECT query that returns a single result.
     *
     * @return the result
     * @throws NoResultException            if there is no result
     * @throws NonUniqueResultException     if more than one result
     * @throws IllegalStateException        if called for a Java
     *                                      Persistence query language UPDATE or DELETE statement
     * @throws QueryTimeoutException        if the query execution exceeds
     *                                      the query timeout value set
     * @throws TransactionRequiredException if a lock mode has
     *                                      been set and there is no transaction
     * @throws PessimisticLockException     if pessimistic locking
     *                                      fails and the transaction is rolled back
     * @throws LockTimeoutException         if pessimistic locking
     *                                      fails and only the statement is rolled back
     */
    public Object getSingleResult();

    /**
     * Execute an update or delete statement.
     *
     * @return the number of entities updated or deleted
     * @throws IllegalStateException        if called for a Java
     *                                      Persistence query language SELECT statement or for
     *                                      a criteria query
     * @throws TransactionRequiredException if there is
     *                                      no transaction
     * @throws QueryTimeoutException        if the statement execution
     *                                      exceeds the query timeout value set
     */
    public int executeUpdate();

    /**
     * Set the maximum number of results to retrieve.
     *
     * @param maxResult
     * @return the same query instance
     * @throws IllegalArgumentException if argument is negative
     */
    public Query setMaxResults(int maxResult);

    /**
     * The maximum number of results the query object was set to
     * retrieve. Returns Integer.MAX_VALUE if setMaxResults was not
     * applied to the query object.
     *
     * @return maximum number of results
     */
    public int getMaxResults();

    /**
     * Set the position of the first result to retrieve.
     *
     * @param start position of the first result, numbered from 0
     * @return the same query instance
     * @throws IllegalArgumentException if argument is negative
     */
    public Query setFirstResult(int startPosition);

    /**
     * The position of the first result the query object was set to
     * retrieve. Returns 0 if setFirstResult was not applied to the
     * query object.
     *
     * @return position of first result
     */
    public int getFirstResult();

    /**
     * Set a query hint.
     * If a vendor-specific hint is not recognized, it is silently
     * ignored.
     * Portable applications should not rely on the standard timeout
     * hint. Depending on the database in use and the locking
     * mechanisms used by the provider, the hint may or may not
     * be observed.
     *
     * @param hintName
     * @param value
     * @return the same query instance
     *         <p/>
     *         * @throws IllegalArgumentException if the second argument is not
     *         valid for the implementation
     */
    public Query setHint(String hintName, Object value);

    /**
     * Get the hints and associated values that are in effect for
     * the query instance.
     *
     * @return query hints
     */
    public Map<String, Object> getHints();

    /**
     * Get the names of the hints that are supported for query
     * objects. These hints correspond to hints that may be passed
     * to the methods of the Query interface that take hints as
     * arguments or used with the NamedQuery and NamedNativeQuery
     * annotations. These include all standard query hints as well as
     * vendor-specific hints supported by the provider. These hints
     * may or may not currently be in effect.
     *
     * @return hints
     */
    public Set<String> getSupportedHints();

    /**
     * Bind an argument to a named parameter.
     *
     * @param name  the parameter name
     * @param value
     * @return the same query instance
     * @throws IllegalArgumentException if parameter name does not
     *                                  correspond to parameter in query string
     *                                  or argument is of incorrect type
     */
    public Query setParameter(String name, Object value);

    /**
     * Bind an instance of java.util.Date to a named parameter.
     *
     * @param name
     * @param value
     * @param temporalType
     * @return the same query instance
     * @throws IllegalArgumentException if parameter name does not
     *                                  correspond to parameter in query string
     */
    public Query setParameter(String name, Date value,
                              TemporalType temporalType);

    /**
     * Bind an instance of java.util.Calendar to a named parameter.
     *
     * @param name
     * @param value
     * @param temporalType
     * @return the same query instance
     * @throws IllegalArgumentException if parameter name does not
     *                                  correspond to parameter in query string
     */
    public Query setParameter(String name, Calendar value,
                              TemporalType temporalType);

    /**
     * Bind an argument to a positional parameter.
     *
     * @param position
     * @param value
     * @return the same query instance
     * @throws IllegalArgumentException if position does not
     *                                  correspond to positional parameter of query
     *                                  or argument is of incorrect type
     */
    public Query setParameter(int position, Object value);

    /**
     * Bind an instance of java.util.Date to a positional parameter.
     *
     * @param position
     * @param value
     * @param temporalType
     * @return the same query instance
     * @throws IllegalArgumentException if position does not
     *                                  correspond to positional parameter of query
     */
    public Query setParameter(int position, Date value,
                              TemporalType temporalType);

    /**
     * Bind an instance of java.util.Calendar to a positional
     * parameter.
     *
     * @param position
     * @param value
     * @param temporalType
     * @return the same query instance
     * @throws IllegalArgumentException if position does not
     *                                  correspond to positional parameter of query
     */
    public Query setParameter(int position, Calendar value,
                              TemporalType temporalType);

    /**
     * Get the parameters names and associated values of the
     * parameters that are bound for the query instance.
     * Returns empty map if no parameters have been bound
     * or if the query does not use named parameters.
     *
     * @return named parameters
     */
    public Map<String, Object> getNamedParameters();

    /**
     * Get the values of the positional parameters
     * that are bound for the query instance.
     * Positional positions are listed in order of position.
     * Returns empty list if no parameters have been bound
     * or if the query does not use positional parameters.
     *
     * @return positional parameters
     */
    public List getPositionalParameters();

    /**
     * Set the flush mode type to be used for the query execution.
     * The flush mode type applies to the query regardless of the
     * flush mode type in use for the entity manager.
     *
     * @param flushMode
     */
    public Query setFlushMode(FlushModeType flushMode);

    /**
     * The flush mode in effect for the query execution. If a flush
     * mode has not been set for the query object, returns the flush
     * mode in effect for the entity manager.
     *
     * @return flush mode
     */
    public FlushModeType getFlushMode();

    /**
     * Set the lock mode type to be used for the query execution.
     *
     * @param lockMode
     * @throws IllegalStateException if not a Java Persistence
     *                               query language SELECT query or Criteria API query
     */
    public Query setLockMode(LockModeType lockMode);

    /**
     * Get the current lock mode for the query.
     *
     * @return lock mode
     * @throws IllegalStateException if not a Java Persistence
     *                               query language SELECT query or Criteria API query
     */
    public LockModeType getLockMode();

    /**
     * Return an object of the specified type to allow access to the
     * provider-specific API. If the provider's Query implementation
     * does not support the specified class, the PersistenceException
     * is thrown.
     *
     * @param cls the class of the object to be returned. This is
     *            normally either the underlying Query implementation class
     *            or an interface that it implements.
     * @return an instance of the specified class
     * @throws PersistenceException if the provider does not support
     *                              the call.
     */
    public <T> T unwrap(Class<T> cls);
}
