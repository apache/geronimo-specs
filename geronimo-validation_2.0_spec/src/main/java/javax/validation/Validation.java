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

import org.apache.geronimo.osgi.locator.ProviderLocator;

import javax.validation.bootstrap.GenericBootstrap;
import javax.validation.bootstrap.ProviderSpecificBootstrap;
import javax.validation.spi.BootstrapState;
import javax.validation.spi.ValidationProvider;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * Note: From Section 4.4.5 Validation of the 1.0 PFD Spec - Validation
 * implementations may only provide the following three public static methods:
 * buildDefaultValidatorFactory(), byDefaultProvider(), byProvider()
 *
 * @version $Rev$ $Date$
 */
public class Validation {

    public static ValidatorFactory buildDefaultValidatorFactory() {
        return byDefaultProvider().configure().buildValidatorFactory();
    }

    public static GenericBootstrap byDefaultProvider() {
        return new GenericBootstrapImpl();
    }

    public static <T extends Configuration<T>, U extends ValidationProvider<T>>
            ProviderSpecificBootstrap<T> byProvider(Class<U> providerType) {
        return new ProviderSpecificBootstrapImpl<T, U>(providerType);
    }

    /*
     * (non-Javadoc) See Section 4.4.5 Validation - Must be private
     *
     * Geronimo implementation specific code.
     */
	private static class ProviderSpecificBootstrapImpl<T extends Configuration<T>, U extends ValidationProvider<T>>
        implements ProviderSpecificBootstrap<T> {

		private final Class<U> providerClass;
		private ValidationProviderResolver vpResolver;

        /*
         * (non-Javadoc)
         *
         * @see javax.validation.bootstrap.ProviderSpecificBootstrap#ProviderSpecificBootstrap(Class<T>)
         */

		public ProviderSpecificBootstrapImpl(Class<U> validationProviderClass) {
			providerClass = validationProviderClass;
        }

        /*
         * (non-Javadoc)
         *
         * @see javax.validation.bootstrap.ProviderSpecificBootstrap#providerResolver(javax.validation.ValidationProviderResolver)
         */
        public ProviderSpecificBootstrap<T> providerResolver(ValidationProviderResolver resolver) {
            vpResolver = resolver;
            return this;
        }

        /*
         * (non-Javadoc)
         *
         * @see javax.validation.bootstrap.ProviderSpecificBootstrap#configure()
         */
        public T configure() {
            if (providerClass == null)
                throw new ValidationException("No resolver provided");

            // create a default resolver if not supplied by providerResolver()
            GenericBootstrapImpl impl = new GenericBootstrapImpl();
            if ( vpResolver == null )
                vpResolver = impl.getDefaultValidationProviderResolver();
            else
                impl.providerResolver(vpResolver);

            // check each provider discovered by the resolver
            for (ValidationProvider<?> vProvider : vpResolver.getValidationProviders()) {
                if (providerClass.isAssignableFrom(vProvider.getClass())) {
                    // Create a ValidationProvider<T> from the above bootstrap impl
                    // and configurationType
                    return providerClass.cast(vProvider).createSpecializedConfiguration(impl);
                }
            }

            // throw a Spec required exception
            throw new ValidationException("No provider found for " + providerClass);
        }
    }

    /*
     * (non-Javadoc) See Section 4.4.5 Validation - Must be private
     *
     * Geronimo implementation specific code.
     */
    private static class GenericBootstrapImpl implements GenericBootstrap, BootstrapState {

        private ValidationProviderResolver vpDefaultResolver;
        private ValidationProviderResolver vpResolver;

        /*
         * (non-Javadoc)
         *
         * @see javax.validation.bootstrap.GenericBootstrap#providerResolver(javax.validation.ValidationProviderResolver)
         */
        public GenericBootstrap providerResolver(ValidationProviderResolver resolver) {
            vpResolver = resolver;
            return this;
        }

        /*
         * (non-Javadoc)
         *
         * @see javax.validation.spi.BootstrapState#getValidationProviderResolver()
         */
        public ValidationProviderResolver getValidationProviderResolver() {
            return vpResolver;
        }

        /*
         * (non-Javadoc)
         *
         * @see javax.validation.spi.BootstrapState#getDefaultValidationProviderResolver()
         */
        public ValidationProviderResolver getDefaultValidationProviderResolver() {
            if (vpDefaultResolver == null)
                vpDefaultResolver = new DefaultValidationProviderResolver();
            return vpDefaultResolver;
        }

        /*
         * (non-Javadoc)
         *
         * @see javax.validation.bootstrap.GenericBootstrap#configure()
         */
        public Configuration<?> configure() {
            ValidationProviderResolver resolv = vpResolver;
            try {
                if (resolv == null)
                    resolv = getDefaultValidationProviderResolver();
                return resolv.getValidationProviders().get(0).createGenericConfiguration(this);
            } catch (Exception e) {
                throw new ValidationException("Could not create Configuration.", e);
            }
        }
    }

    /*
     * (non-Javadoc) See Section 4.4.5 Validation - Must be private
     *
     * Geronimo implementation specific code.
     */
    private static class DefaultValidationProviderResolver implements ValidationProviderResolver {
        // cache of providers per class loader
        private volatile WeakHashMap<ClassLoader, List<ValidationProvider<?>>> providerCache =
            new WeakHashMap<ClassLoader, List<ValidationProvider<?>>>();

        /*
         * (non-Javadoc)
         *
         * @see javax.validation.ValidationProviderResolver#getValidationProviders()
         */
        public List<ValidationProvider<?>> getValidationProviders() {
            List<ValidationProvider<?>> providers;

            // get our class loader
            ClassLoader cl = PrivClassLoader.get(null);
            if (cl == null)
                cl = PrivClassLoader.get(DefaultValidationProviderResolver.class);

            // use any previously cached providers
            providers = providerCache.get(cl);
            if (providers == null) {
                // need to discover and load them for this class loader
                providers = new ArrayList<ValidationProvider<?>>();
                try {
                    List<Object> serviceProviders = ProviderLocator.getServices(ValidationProvider.class.getName(), this.getClass(), cl);
                    for (Object provider : serviceProviders) {
                        // create an instance to return
                        providers.add((ValidationProvider<?>) provider);
                    }
                } catch (ClassNotFoundException e) {
                    throw new ValidationException("Failed to load provider", e);
                } catch (InstantiationException e) {
                    throw new ValidationException("Failed to instantiate provider", e);
                } catch (IllegalAccessException e) {
                    throw new ValidationException("Failed to access provider", e);
                } catch (ClassCastException e) {
                    throw new ValidationException("Invalid provider definition", e);
                } catch (Exception e) {
                    throw new ValidationException("Failed to instantiate provider", e);
                }

                // cache the discovered providers
                providerCache.put(cl, providers);
            }

            // caller must handle the case of no providers found
            return providers;
        }

        private static class PrivClassLoader implements PrivilegedAction<ClassLoader> {
            private final Class<?> c;

            public static ClassLoader get(Class<?> c) {
                final PrivClassLoader action = new PrivClassLoader(c);
                if (System.getSecurityManager() != null)
                    return AccessController.doPrivileged(action);
                else
                    return action.run();
            }

            private PrivClassLoader(Class<?> c) {
                this.c = c;
            }

            public ClassLoader run() {
                if (c != null)
                    return c.getClassLoader();
                else
                    return Thread.currentThread().getContextClassLoader();
            }
        }
    }
}

