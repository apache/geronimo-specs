/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package javax.enterprise.inject.spi;

/**
 * Fired before registering producer field.
 * 
 * @version $Rev$ $Date$
 *
 * @param <X> bean class
 * @param <T> producer field return type
 */
public interface ProcessProducerField<X, T> extends ProcessBean<T>
{
    /**
     * Returns annotated field.
     * 
     * @return annotated field.
     */
    AnnotatedField<X> getAnnotatedProducerField();

    /**
     * @return the {@link javax.enterprise.inject.Disposes} annotated parameter of the disposal method
     *      which fits the producer field, or <code>null</code> if there is no disposal method.
     */
    AnnotatedParameter<X> getAnnotatedDisposedParameter();
}