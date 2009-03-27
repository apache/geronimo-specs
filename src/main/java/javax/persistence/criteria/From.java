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

package javax.persistence.criteria;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Collection;
import javax.persistence.metamodel.List;
import javax.persistence.metamodel.Map;
import javax.persistence.metamodel.Set;

public interface From<Z, X> extends Path<X>, FetchParent<Z, X> {

    java.util.Set<Join<X, ?>> getJoins();

    <Y> Join<X, Y> join(Attribute<? super X, Y> attribute);

    <Y> Join<X, Y> join(Attribute<? super X, Y> attribute, JoinType jt);

    <Y> CollectionJoin<X, Y> join(Collection<? super X, Y> collection);

    <Y> SetJoin<X, Y> join(Set<? super X, Y> set);

    <Y> ListJoin<X, Y> join(List<? super X, Y> list);

    <K, V> MapJoin<X, K, V> join(Map<? super X, K, V> map);

    <Y> CollectionJoin<X, Y> join(Collection<? super X, Y> collection,
        JoinType jt);

    <Y> SetJoin<X, Y> join(Set<? super X, Y> set, JoinType jt);

    <Y> ListJoin<X, Y> join(List<? super X, Y> list, JoinType jt);

    <K, V> MapJoin<X, K, V> join(Map<? super X, K, V> map, JoinType jt);

    //String-based:

    <W, Y> Join<W, Y> join(String attributeName);

    <W, Y> CollectionJoin<W, Y> joinCollection(String attributeName);

    <W, Y> SetJoin<W, Y> joinSet(String attributeName);

    <W, Y> ListJoin<W, Y> joinList(String attributeName);

    <W, K, V> MapJoin<W, K, V> joinMap(String attributeName);

    <W, Y> Join<W, Y> join(String attributeName, JoinType jt);

    <W, Y> CollectionJoin<W, Y> joinCollection(String attributeName,
        JoinType jt);

    <W, Y> SetJoin<W, Y> joinSet(String attributeName, JoinType jt);

    <W, Y> ListJoin<W, Y> joinList(String attributeName, JoinType jt);

    <W, K, V> MapJoin<W, K, V> joinMap(String attributeName, JoinType jt);
}
