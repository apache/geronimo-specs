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

import javax.persistence.metamodel.AbstractCollection;
import javax.persistence.metamodel.Attribute;

public interface FetchParent<Z, X> {

    java.util.Set<Fetch<X, ?>> getFetches();

    <Y> Fetch<X, Y> fetch(Attribute<? super X, Y> assoc);

    <Y> Fetch<X, Y> fetch(Attribute<? super X, Y> assoc, JoinType jt);

    <Y> Fetch<X, Y> fetch(AbstractCollection<? super X, ?, Y> assoc);

    <Y> Fetch<X, Y> fetch(AbstractCollection<? super X, ?, Y> assoc,
        JoinType jt);

    //String-based:

    <Y> Fetch<X, Y> fetch(String assocName);

    <Y> Fetch<X, Y> fetch(String assocName, JoinType jt);
}
