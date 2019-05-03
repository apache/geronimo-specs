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
package jakarta.enterprise.inject.spi;

import jakarta.enterprise.inject.spi.configurator.InjectionPointConfigurator;

/**
 * Gets fired for each InjectionPoint.
 *
 * @param <T> managed bean class
 * @param <X> declared type of the injection point
 */
public interface ProcessInjectionPoint<T, X>
{
    /**
     * @return the InjectionPoint created from originally parsing the AnnotatedType.
     */
    InjectionPoint getInjectionPoint();

    /**
     * Replace the original InjectionPoint point with the given one.
     *
     * You can only either use {@link #setInjectionPoint(InjectionPoint)} or
     * {@link #configureInjectionPoint()}
     * @param injectionPoint
     */
    void setInjectionPoint(InjectionPoint injectionPoint);

    /**
     * Adding definition error. Container aborts
     * processing after calling all observers.
     *
     * @param t throwable
     */
    void addDefinitionError(Throwable t);

   /**
    *  Creates a new configurator for this injection point
    *  initialised with the information from the processed injectionPoint
    *  You can only either use {@link #setInjectionPoint(InjectionPoint)} or
    *  {@link #configureInjectionPoint()}
    *
    * @return an {@code InjectionPointConfigurator} to tweak the current InjectionPoint
    */
    InjectionPointConfigurator configureInjectionPoint();
}
