/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package javax.enterprise.inject;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.enterprise.util.TypeLiteral;
import javax.inject.Provider;

/**
 * The <code>Instance</code> interface provides a method for obtaining 
 * instances of beans with required types and qualifiers.
 * 
 * @version $Rev$ $Date$
 *
 * @param <T> bean required type
 */
public interface Instance<T> extends Iterable<T>, Provider<T>
{    
    /**
     * Creates new <code>Instance</code> with given
     * qualifiers. 
     * 
     * @param qualifiers
     * @return new child instance with given qualifiers.
     */
    Instance<T> select(Annotation... qualifiers);
    
    /**
     * Returns new child instance with given class and qualifiers.
     * 
     * @param <U> subtype info
     * @param subtype subtype class
     * @param qualifiers qualifiers
     * @return new child instance with given class and qualifiers
     */
    <U extends T> Instance<U> select(Class<U> subtype, Annotation... qualifiers);
    
    /**
     * Return new child instance with given class info and qualifiers.
     * 
     * @param <U> subtype info
     * @param subtype subtype class
     * @param qualifiers qualifiers
     * @return new child instance with given class info and qualifiers
     */
    <U extends T> Instance<U> select(TypeLiteral<U> subtype, Annotation... qualifiers);
    
    /**
     * Return true if resolution is unsatisfied, false otherwise.
     * 
     * @return true if resolution is unsatisfied, false otherwise
     */
    boolean isUnsatisfied();

    /**
     * Returns true if resolution is ambiguous, false otherwise.
     * 
     * @return true if resolution is ambiguous, false otherwise.
     */
    boolean isAmbiguous();

    /**
     * @return {@code true} if there is exactly one {@link javax.enterprise.inject.spi.Bean}
     *      which resolves according to the currently selected type and qualifiers.
     *      In other words: whether the Instance can serve an actual Contextual Reference.
     *
     * @since 2.0
     */
    default boolean isResolvable()
    {
        return !isAmbiguous() && !isUnsatisfied();
    }

    /**
     * @return a Stream of all the beans available for the currently selected type and qualifiers,
     *      or an empty Stream if {@link #isUnsatisfied()}
     */
    default Stream<T> stream()
    {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * Destroy the given Contextual Instance.
     * This is especially intended for {@link javax.enterprise.context.Dependent} scoped beans
     * which might otherwise create mem leaks.
     * @param instance
     */
    void destroy(T instance);


}
