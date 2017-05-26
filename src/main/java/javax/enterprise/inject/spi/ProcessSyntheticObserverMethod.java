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
 * This event only gets fired for custom ObserverMethod added via {@link AfterBeanDiscovery}.
 * The event gets fired before registering the ObserverMethod with the container.
 * 
 * @version $Rev: 1796096 $ $Date: 2017-05-24 22:27:18 +0200 (Wed, 24 May 2017) $
 *
 * @param <EVENTTYPE> observed event type
 * @param <BEANCLASS> bean class
 */
public interface ProcessSyntheticObserverMethod<EVENTTYPE, BEANCLASS> extends ProcessObserverMethod<EVENTTYPE, BEANCLASS>
{
    /**
     * @return the Extension instance adding the very ObserverMethod
     */
    Extension getSource();
}