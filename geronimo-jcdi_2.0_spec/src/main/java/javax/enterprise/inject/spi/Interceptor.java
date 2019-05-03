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

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.interceptor.InvocationContext;


/**
 * A Bean for creating and using an interceptor.
 * A CDI interceptor is always a separate dependent instance
 * for each intercepted class. It will get created when the
 * intercepted bean gets created and destroyed when the
 * intercepted bean gets destroyed.
 *
 * @param <T>
 */
public interface Interceptor<T> extends Bean<T>
{
    /**
     * Usually a single Interceptor
     * @return all {@link javax.interceptor.InterceptorBinding}s handled by this interceptor.
     */
    Set<Annotation> getInterceptorBindings();

    /**
     * @param type InterceptionType in question
     * @return <code>true</code> if this interceptor handles the given InterceptionType, <code>false</code> otherwise
     */
    boolean intercepts(InterceptionType type);

    /**
     * Perform the interception. This will e.g. invoke the &#064;AroundInvoke annotated method
     * on the given instance of T.
     * @param type the InterceptionType. This is important if an interceptor has multiple interceptor methods
     *             e.g. one &#064;AroundInvoke and one &#064;PostConstruct;
     * @param instance the interceptor instance
     * @param ctx the InvocationContext contains all the interceptor chain state for a single invocation.
     * @return the object or wrapper type returned by the intercepted instance or the previous interceptor
     *         (if this is not the last interceptor in the chain)
     * @throws Exception wrapped from the intercepted instance. See CDI-115
     */
    Object intercept(InterceptionType type, T instance, InvocationContext ctx) throws Exception;

}
