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

public interface PredicateOperand {

    Predicate equal(PredicateOperand arg);

    Predicate equal(Class cls);

    Predicate equal(Number arg);

    Predicate equal(String arg);

    Predicate equal(boolean arg);

    Predicate equal(Date arg);

    Predicate equal(Calendar arg);

    Predicate equal(Enum<?> e);

    Predicate notEqual(PredicateOperand arg);

    Predicate notEqual(Class cls);

    Predicate notEqual(Number arg);

    Predicate notEqual(String arg);

    Predicate notEqual(boolean arg);

    Predicate notEqual(Date arg);

    Predicate notEqual(Calendar arg);

    Predicate notEqual(Enum<?> e);

    Predicate greaterThan(PredicateOperand arg);

    Predicate greaterThan(Number arg);

    Predicate greaterThan(String arg);

    Predicate greaterThan(Date arg);

    Predicate greaterThan(Calendar arg);

    Predicate greaterEqual(PredicateOperand arg);

    Predicate greaterEqual(Number arg);

    Predicate greaterEqual(String arg);

    Predicate greaterEqual(Date arg);

    Predicate greaterEqual(Calendar arg);

    Predicate lessThan(PredicateOperand arg);

    Predicate lessThan(Number arg);

    Predicate lessThan(String arg);

    Predicate lessThan(Date arg);

    Predicate lessThan(Calendar arg);

    Predicate lessEqual(PredicateOperand arg);

    Predicate lessEqual(Number arg);

    Predicate lessEqual(String arg);

    Predicate lessEqual(Date arg);

    Predicate lessEqual(Calendar arg);

    Predicate between(PredicateOperand arg1, PredicateOperand arg2);

    Predicate between(PredicateOperand arg1, Number arg2);

    Predicate between(Number arg1, PredicateOperand arg2);

    Predicate between(Number arg1, Number arg2);

    Predicate between(PredicateOperand arg1, String arg2);

    Predicate between(String arg1, PredicateOperand arg2);

    Predicate between(String arg1, String arg2);

    Predicate between(PredicateOperand arg1, Date arg2);

    Predicate between(Date arg1, PredicateOperand arg2);

    Predicate between(Date arg1, Date arg2);

    Predicate between(PredicateOperand arg1, Calendar arg2);

    Predicate between(Calendar arg1, PredicateOperand arg2);

    Predicate between(Calendar arg1, Calendar arg2);

    Predicate like(PredicateOperand pattern);

    Predicate like(PredicateOperand pattern, PredicateOperand escapeChar);

	Predicate like(PredicateOperand pattern, char escapeChar);

	Predicate like(String pattern);

	Predicate like(String pattern, PredicateOperand escapeChar);

	Predicate like(String pattern, char escapeChar);
}
