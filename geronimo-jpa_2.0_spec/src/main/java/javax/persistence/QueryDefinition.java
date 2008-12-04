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

public interface QueryDefinition extends Subquery {

    DomainObject addRoot(Class cls);

    DomainObject addSubqueryRoot(PathExpression path);

    QueryDefinition select(SelectItem... selectItems);

    QueryDefinition select(List<SelectItem> selectItemList);

    QueryDefinition selectDistinct(SelectItem... selectItems);

    QueryDefinition selectDistinct(List<SelectItem> selectItemList);

    QueryDefinition where(Predicate predicate);

    QueryDefinition orderBy(OrderByItem... orderByItems);

    QueryDefinition orderBy(List<OrderByItem> orderByItemList);

    QueryDefinition groupBy(PathExpression... pathExprs);

    QueryDefinition groupBy(List<PathExpression> pathExprList);

    QueryDefinition having(Predicate predicate);

    SelectItem newInstance(Class cls, SelectItem... args);

    Predicate exists();

    Subquery all();

    Subquery any();

    Subquery some();

    CaseExpression generalCase();

    CaseExpression simpleCase(Expression caseOperand);

    CaseExpression simpleCase(Number caseOperand);

    CaseExpression simpleCase(String caseOperand);

    CaseExpression simpleCase(Date caseOperand);

    CaseExpression simpleCase(Calendar caseOperand);

    CaseExpression simpleCase(Class caseOperand);

    CaseExpression simpleCase(Enum<?> caseOperand);

    Expression coalesce(Expression... exp);

    Expression coalesce(String... exp);

    Expression coalesce(Date... exp);

    Expression coalesce(Calendar... exp);

    Expression nullif(Expression exp1, Expression exp2);

    Expression nullif(Number arg1, Number arg2);

    Expression nullif(String arg1, String arg2);

    Expression nullif(Date arg1, Date arg2);

    Expression nullif(Calendar arg1, Calendar arg2);

    Expression nullif(Class arg1, Class arg2);

    Expression nullif(Enum<?> arg1, Enum<?> arg2);

    Predicate predicate(boolean b);

    Expression currentTime();

    Expression currentDate();

    Expression currentTimestamp();

    Expression literal(String s);

    Expression literal(Number n);

	Expression literal(boolean b);

	Expression literal(Calendar c);

	Expression literal(Date d);

	Expression literal(char c);

	Expression literal(Class cls);

	Expression literal(Enum<?> e);

	Expression nullLiteral();

	Expression param(String name);
}
