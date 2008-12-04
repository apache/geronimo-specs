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

public interface CaseExpression {
    CaseExpression when(Predicate pred);

    CaseExpression when(Expression when);

    CaseExpression when(Number when);

    CaseExpression when(String when);

    CaseExpression when(Date when);

    CaseExpression when(Calendar when);

    CaseExpression when(Class when);

    CaseExpression when(Enum<?> when);

    CaseExpression then(Expression then);

    CaseExpression then(Number then);

    CaseExpression then(String then);

    CaseExpression then(Date then);

    CaseExpression then(Calendar then);

    CaseExpression then(Class then);

    CaseExpression then(Enum<?> then);

    Expression elseCase(Expression arg);

    Expression elseCase(String arg);

    Expression elseCase(Number arg);

    Expression elseCase(Date arg);

	Expression elseCase(Calendar arg);

	Expression elseCase(Class arg);

	Expression elseCase(Enum<?> arg);
}
