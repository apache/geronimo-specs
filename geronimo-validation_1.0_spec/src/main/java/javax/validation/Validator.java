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

import java.util.Set;
import javax.validation.metadata.BeanDescriptor;

/**
 * @version $Rev$ $Date$
 */
public interface Validator {
    <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups);

    <T> Set<ConstraintViolation<T>> validateProperty(T object,
        String propertyName,
        Class<?>... groups);

    <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType,
        String propertyName,
        Object value,
        Class<?>... groups);

    BeanDescriptor getConstraintsForClass(Class<?> clazz);

    public <T> T unwrap(Class<T> type);
}

