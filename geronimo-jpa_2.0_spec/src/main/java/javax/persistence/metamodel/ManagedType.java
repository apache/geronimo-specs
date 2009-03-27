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
package javax.persistence.metamodel;

public interface ManagedType<X> extends Type<X>, Bindable<X> {

    <Y> Attribute<? super X, Y> getAttribute(String name,
        Class<Y> type);

    <Y> Attribute<X, Y> getDeclaredAttribute(String name,
        Class<Y> type);

    java.util.Set<Attribute<? super X, ?>> getAttributes();

    java.util.Set<Attribute<X, ?>> getDeclaredAttributes();

    <E> Collection<? super X, E> getCollection(String name,
        Class<E> elementType);

    <E> Set<? super X, E> getSet(String name, Class<E> elementType);

    <E> List<? super X, E> getList(String name, Class<E> elementType);

    <K, V> Map<? super X, K, V> getMap(String name,
        Class<K> keyType,
        Class<V> valueType);

    <E> Collection<X, E> getDeclaredCollection(String name,
        Class<E> elementType);

    <E> Set<X, E> getDeclaredSet(String name, Class<E> elementType);

    <E> List<X, E> getDeclaredList(String name, Class<E> elementType);

    <K, V> Map<X, K, V> getDeclaredMap(String name,
        Class<K> keyType,
        Class<V> valueType);

    java.util.Set<AbstractCollection<? super X, ?, ?>> getCollections();

    java.util.Set<AbstractCollection<X, ?, ?>> getDeclaredCollections();
    
    //String-based:

    Attribute<? super X, ?> getAttribute(String name);

    Attribute<X, ?> getDeclaredAttribute(String name);

    Collection<? super X, ?> getCollection(String name);

    Set<? super X, ?> getSet(String name);

    List<? super X, ?> getList(String name);

    Map<? super X, ?, ?> getMap(String name);

    Collection<X, ?> getDeclaredCollection(String name);

    Set<X, ?> getDeclaredSet(String name);

    List<X, ?> getDeclaredList(String name);

    Map<X, ?, ?> getDeclaredMap(String name);
}
