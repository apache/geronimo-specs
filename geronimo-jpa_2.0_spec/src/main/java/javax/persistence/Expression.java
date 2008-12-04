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

public interface Expression extends SelectItem, PredicateOperand {

    Predicate member(PathExpression arg);

    Predicate isNull();

    Predicate in(String... strings);

    Predicate in(Number... nums);

    Predicate in(Enum<?>... enums);

    Predicate in(Class... classes);

    Predicate in(Expression... params);

    Predicate in(Subquery subquery);

    Expression length();

    Expression concat(String... str);

    Expression concat(Expression... str);

    Expression substring(int start);

    Expression substring(Expression start);

    Expression substring(int start, int len);

    Expression substring(int start, Expression len);

    Expression substring(Expression start, int len);

    Expression substring(Expression start, Expression len);

    Expression lower();

    Expression upper();

    Expression trim();

    Expression trim(TrimSpec spec);

    Expression trim(char c);

    Expression trim(char c, TrimSpec spec);

    Expression trim(Expression expr);

    Expression trim(Expression expr, TrimSpec spec);

    Expression locate(String str);

    Expression locate(Expression str);

    Expression locate(String str, int position);

    Expression locate(String str, Expression position);

    Expression locate(Expression str, int position);

    Expression locate(Expression str, Expression position);

    Expression plus(Number num);

    Expression plus(Expression expr);

    Expression minus();

    Expression minus(Number num);

    Expression minus(Expression expr);

    Expression dividedBy(Number num);

    Expression dividedBy(Expression expr);

    Expression times(Number num);

	Expression times(Expression expr);

	Expression abs();

	Expression sqrt();

	Expression mod(int num);

	Expression mod(Expression expr);
}
