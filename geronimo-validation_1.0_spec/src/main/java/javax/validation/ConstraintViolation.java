/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package javax.validation;

/**
 */
public interface ConstraintViolation<T> {

    /**
     * @return The interpolated error message for this constraint violation.
     */
    String getMessage();

    /**
     * @return String
     */
    String getMessageTemplate();

    /**
     * @return root bean
     */
    T getRootBean();

    /**
     * @return Class
     */
    Class<T> getRootBeanClass();

    /**
     * @return Object
     */
    Object getLeafBean();

    /**
     * @return String
     */
    String getPropertyPath();

    /**
     * @return Object
     */
    Object getInvalidValue();

    /**
     * @return ConstraintDescriptor
     */
    ConstraintDescriptor<?> getConstraintDescriptor();
}
