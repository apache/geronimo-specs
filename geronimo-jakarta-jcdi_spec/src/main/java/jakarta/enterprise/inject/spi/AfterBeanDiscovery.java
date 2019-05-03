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

import jakarta.enterprise.context.spi.Context;
import jakarta.enterprise.inject.spi.configurator.BeanConfigurator;
import jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator;

/**
 * Events that are fired after discovery bean process.
 * 
 * @version $Rev$ $Date$
 *
 */
public interface AfterBeanDiscovery 
{
    /**
     * Adds definition error. Container aborts deployment after
     * all observer methods are called.
     * 
     * @param t throwable
     */
    void addDefinitionError(Throwable t);

    /**
     * Registering the bean with container.
     * 
     * @param bean new bean
     */
    void addBean(Bean<?> bean);
    
    /**
     * Registers the given observer method with container.
     * 
     * @param observerMethod observer method
     */
    void addObserverMethod(ObserverMethod<?> observerMethod);
    
    /**
     * Adds given context to the container.
     * 
     * @param context new context
     */
    void addContext(Context context);

    /**
     * This will return the AnnotatedType including all changes applied by CDI Extensions.
     *
     * @param type
     * @param id the id of the AnnotatedType registered by {@link BeforeBeanDiscovery#addAnnotatedType(AnnotatedType, String)}
     *           or <code>null</code> for the one scanned
     * @param <T>
     * @return the AnnotatedType for the given type and id.
     */
    <T> AnnotatedType<T> getAnnotatedType(Class<T> type, String id);

    /**
     * @return an Iterable of all AnnotatedTypes which implement the given type
     */
    <T> Iterable<AnnotatedType<T>> getAnnotatedTypes(Class<T> type);

   /**
    * Creates a bean configurator to configure a new bean.
    *
    * A ProcessBean is fired once the bean has been built.
    *
    * The bean configurator created is meant as a one time use object.
    * Invoke this method to create new Beans.
    *
    * @throws IllegalStateException if used outside of the observer method's invocation
    * @return a fresh {@code BeanConfigurator} to configure a new bean.
    */
    <T> BeanConfigurator<T> addBean();

   /**
    * Creates an observer method configurator to define an observer method.
    *
    * A ProcessObserverMethod is invoked once the observer method is built.
    *
    * The observer method configurator created is meant as a one time use object.  Invoke this method to create new ones.
    *
    * @param <T>
    * @throws IllegalStateException if used outside of the observer method's invocation
    * @return an observer method configurator to define an observer method.
    */
    <T> ObserverMethodConfigurator<T> addObserverMethod();
}
