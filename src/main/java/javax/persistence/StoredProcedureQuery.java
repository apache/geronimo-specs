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

public interface StoredProcedureQuery extends Query {

    StoredProcedureQuery setHint(String hintName, Object value);

    <T> StoredProcedureQuery setParameter(Parameter<T> param, 
                                          T value);

    StoredProcedureQuery setParameter(Parameter<Calendar> param,
                                      Calendar value, 
                                      TemporalType temporalType);

    StoredProcedureQuery setParameter(Parameter<Date> param, 
                                      Date value, 
                                      TemporalType temporalType);

    StoredProcedureQuery setParameter(String name, Object value);

    StoredProcedureQuery setParameter(String name, 
                                      Calendar value, 
                                      TemporalType temporalType);

    StoredProcedureQuery setParameter(String name, 
                                      Date value, 
                                      TemporalType temporalType);

    StoredProcedureQuery setParameter(int position, Object value);

    StoredProcedureQuery setParameter(int position, 
                                      Calendar value,  
                                      TemporalType temporalType);

    StoredProcedureQuery setParameter(int position, 
                                      Date value,  
                                      TemporalType temporalType);

    StoredProcedureQuery setFlushMode(FlushModeType flushMode);

    StoredProcedureQuery registerStoredProcedureParameter(
	  int position,
	  Class type,
	  ParameterMode mode);

    StoredProcedureQuery registerStoredProcedureParameter(
	  String parameterName,
	  Class type,
	  ParameterMode mode);

    Object getOutputParameterValue(int position);

    Object getOutputParameterValue(String parameterName);

    boolean execute();

    int executeUpdate();

    List getResultList();

    Object getSingleResult();

    boolean hasMoreResults();

    int getUpdateCount();

}
