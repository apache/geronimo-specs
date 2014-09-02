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
    public InjectionPoint getInjectionPoint();

    /**
     * Replace the original InjectionPoint point with the given one.
     * @param injectionPoint
     */
    public void setInjectionPoint(InjectionPoint injectionPoint);

    /**
     * Adding definition error. Container aborts
     * processing after calling all observers.
     *
     * @param t throwable
     */
    public void addDefinitionError(Throwable t);
}
