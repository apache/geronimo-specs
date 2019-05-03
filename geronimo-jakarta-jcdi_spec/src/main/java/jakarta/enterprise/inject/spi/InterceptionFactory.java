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

import jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator;

/**
 * An InterceptionFactory allows to apply CDI proxies for custom beans and
 * Contextual Instances manually created in producer methods.
 *
 * @param <T> the type of the proxy to be created
 */
public interface InterceptionFactory<T>
{
    /**
     * Usually having a non-static final field with any modifier other than private leads to a
     * DeploymentException. This is to prevent user failures as those fields cannot be proxied.
     *
     * In certain situations one want's to provide a CDI bean for such a class nonetheless.
     * By invoking this method on a Bean wich such a class we can effectively prevent the container
     * from treating this situation as a Deployment error.
     */
    InterceptionFactory<T> ignoreFinalMethods();

    /**
     * You can e.g. add an InterceptorBinding annotation, etc
     *
     * @return a Configurator to provide information for the proxy to create
     */
    AnnotatedTypeConfigurator<T> configure();

    /**
     * Wrapps the given originalInstance with a CDI proxy.
     * This will use the CreationalContext of the Bean it got created for.
     *
     * @param originalInstance
     * @return the CDI proxy (Contextual Reference) wrapped around the originalInstance.
     */
    T createInterceptedInstance(T originalInstance);
}
