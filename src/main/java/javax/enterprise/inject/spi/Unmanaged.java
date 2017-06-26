/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package javax.enterprise.inject.spi;

import javax.enterprise.context.spi.CreationalContext;

/**
 * Helper class for manually maintaining CDI created instances
 * which are not managed by the CDI container.
 *
 * Be aware that instances created that way are <i>not</i>
 * managed by the CDI container and thus need to be
 * cleaned up manually to not create memory leaks!.
 *
 * Normal code shall use {@link javax.enterprise.inject.Instance<T>} if possible.
 *
 * The reason for using UnmanagedInstance is for
 * &#064;Dependent scoped instances which should not pollute the
 * {@link javax.enterprise.context.spi.CreationalContext} of the containing instance.
 *
 * Note that the methods of UnmanagedInstance needs to be called
 * in a well defined order.
 *
 * Please note that this classes are not thread safe!
 *
 * @param <T> the type of the CDI instance to create
 * @since 1.1
 */
public class Unmanaged<T>
{
    private BeanManager beanManager;
    private InjectionTarget<T> injectionTarget;


    public Unmanaged(Class<T> clazz)
    {
        this(CDI.current().getBeanManager(), clazz);
    }

    public Unmanaged(BeanManager beanManager, java.lang.Class<T> clazz)
    {
        this.beanManager = beanManager;
        AnnotatedType<T> annotatedType = beanManager.createAnnotatedType(clazz);
        injectionTarget =  beanManager.createInjectionTarget(annotatedType);
    }


    public UnmanagedInstance<T> newInstance()
    {
        return new UnmanagedInstance<>(beanManager, injectionTarget);
    }


    /**
     * This basically delegates to the {@link javax.enterprise.inject.spi.InjectionTarget}
     * interface
     * @param <T> the type of the CDI instance to create
     */
    public static class UnmanagedInstance<T>
    {
        private BeanManager beanManager;
        private InjectionTarget<T> injectionTarget;

        private CreationalContext<T> creationalContext;
        private T instance;

        private boolean injected;
        private boolean disposed;

        private UnmanagedInstance(BeanManager beanManager, InjectionTarget<T> injectionTarget)
        {
            this.injectionTarget = injectionTarget;
            this.beanManager = beanManager;
        }

        /**
         * Create the CDI instance itself. This internally just calls
         * {@link javax.enterprise.inject.spi.InjectionTarget#produce(javax.enterprise.context.spi.CreationalContext)}
         * and performs a few checks upfront.
         *
         * @throws java.lang.IllegalStateException if the instance already got created
         * @throws java.lang.IllegalStateException if the instance already got disposed
         */
        public UnmanagedInstance<T> produce()
        {
            if (creationalContext != null)
            {
                throw new IllegalStateException("UnmanagedInstance is already produced");
            }
            if (disposed)
            {
                throw new IllegalStateException("UnmanagedInstance is already disposed");
            }

            creationalContext = beanManager.createCreationalContext(null);
            instance = injectionTarget.produce(creationalContext);

            return this;
        }

        /**
         * Fill &#064;inject field, constructor and methods.
         *
         * @throws java.lang.IllegalStateException if the instance was not yet created
         * @throws java.lang.IllegalStateException if the instance already got injected
         * @throws java.lang.IllegalStateException if the instance already got disposed
         * @see javax.enterprise.inject.spi.InjectionTarget#inject(Object, javax.enterprise.context.spi.CreationalContext)
         */
        public UnmanagedInstance<T> inject()
        {
            basicCheck();
            if (injected)
            {
                throw new IllegalStateException("UnmanagedInstance is already injected");
            }

            injectionTarget.inject(instance, creationalContext);
            injected = true;

            return this;
        }

        /**
         * Invoke any &#064;PostConstruct methods.
         * @see javax.enterprise.inject.spi.InjectionTarget#postConstruct(Object)
         */
        public UnmanagedInstance<T> postConstruct()
        {
            basicCheck();

            injectionTarget.postConstruct(instance);

            return this;
        }

        /**
         * This method should only get called after the
         * CDI instance got properly produced and initialized
         * via
         * {@link #produce()}
         * {@link #inject()}
         * {@link #postConstruct()}
         *
         * @return the filled cdi instance
         */
        public T get()
        {
            basicCheck();

            return instance;
        }

        /**
         * Invoke any &#064;PreDestroy annotated methods
         * and interceptors of the given CDI instance.
         */
        public UnmanagedInstance<T> preDestroy()
        {
            basicCheck();

            injectionTarget.preDestroy(instance);

            return this;
        }

        /**
         * Dispose the CDI instance. One should call {@link #preDestroy()} before
         * this method.
         * @see javax.enterprise.inject.spi.InjectionTarget#dispose(Object)
         */
        public UnmanagedInstance<T> dispose()
        {
            basicCheck();

            injectionTarget.dispose(instance);
            creationalContext.release();

            disposed = true;
            return this;
        }

        /**
         * Check whether the UnmanagedInstance is already initialized and not yet disposed.
         */
        private void basicCheck()
        {
            if (creationalContext == null)
            {
                throw new IllegalStateException("UnmanagedInstance is not yet initialized. Invoke #produce() first!");
            }
            if (disposed)
            {
                throw new IllegalStateException("UnmanagedInstance is already disposed");
            }
        }
    }

}
