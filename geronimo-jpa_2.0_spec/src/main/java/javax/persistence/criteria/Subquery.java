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

import javax.persistence.metamodel.Collection;
import javax.persistence.metamodel.List;
import javax.persistence.metamodel.Map;
import javax.persistence.metamodel.Set;

public interface Subquery<T> extends AbstractQuery, Expression<T> {

    AbstractQuery getParent();

    Subquery<T> select(Expression<T> expression);

    //override the return type only:

    Subquery<T> where(Expression<Boolean> restriction);

    Subquery<T> where(Predicate... restrictions);

    Subquery<T> groupBy(Expression<?>... grouping);

    Subquery<T> having(Expression<Boolean> restriction);

    Subquery<T> having(Predicate... restrictions);

    Subquery<T> distinct(boolean distinct);

    Expression<T> getSelection();

    <Y> Root<Y> correlate(Root<Y> parentRoot);

    <X, Y> Join<X, Y> correlate(Join<X, Y> parentJoin);

    <X, Y> CollectionJoin<X, Y> correlate(
        CollectionJoin<X, Y> parentCollection);

    <X, Y> SetJoin<X, Y> correlate(SetJoin<X, Y> parentSet);

    <X, Y> ListJoin<X, Y> correlate(ListJoin<X, Y> parentList);

    <X, K, V> MapJoin<X, K, V> correlate(MapJoin<X, K, V> parentMap);

    java.util.Set<Join<?, ?>> getJoins();
}
