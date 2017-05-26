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

import javax.enterprise.inject.spi.configurator.ObserverMethodConfigurator;

/**
 * Fired for each observer.
 * 
 * @version $Rev$ $Date$
 *
 * @param <EVENTTYPE> observed event type
 * @param <BEANCLASS> bean class
 */
public interface ProcessObserverMethod<EVENTTYPE, BEANCLASS>
{
    /**
     * Returns annotated method.
     * 
     * @return annotated method
     */
    AnnotatedMethod<BEANCLASS> getAnnotatedMethod();
    
    /**
     * Returns observer method instance that
     * is called by the container on event. 
     * 
     * @return observer method instance
     */
    ObserverMethod<EVENTTYPE> getObserverMethod();


    /**
     * @param observerMethod to replace the original one
     */
    void setObserverMethod(ObserverMethod<EVENTTYPE> observerMethod);

    /**
     * Tell the container to ignore the current ObserverMethod.
     */
    void veto();

    /**
     * Add throwable.
     * 
     * @param t throwable
     */
    void addDefinitionError(Throwable t);

   /**
    * Creates a configurator for this observer method
    * @return
    */
    ObserverMethodConfigurator<EVENTTYPE> configureObserverMethod();


    
}
