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
package jakarta.persistence.criteria;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.EntityType;

public interface CriteriaUpdate<T> extends CommonAbstractCriteria {

    Root<T> from(Class<T> entityClass);

    Root<T> from(EntityType<T> entity);

    Root<T> getRoot();

    <Y, X extends Y> CriteriaUpdate<T> set(SingularAttribute<? super T, Y> attribute, X value);

    <Y> CriteriaUpdate<T> set(SingularAttribute<? super T, Y> attribute, Expression<? extends Y> value);

    <Y, X extends Y> CriteriaUpdate<T> set(Path<Y> attribute, X value);

    <Y> CriteriaUpdate<T> set(Path<Y> attribute, Expression<? extends Y> value);

    CriteriaUpdate<T> set(String attributeName, Object value);

    CriteriaUpdate<T> where(Expression<Boolean> restriction);

    CriteriaUpdate<T> where(Predicate... restrictions);
 }
