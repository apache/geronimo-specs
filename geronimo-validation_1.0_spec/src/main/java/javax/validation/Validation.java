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

import java.util.List;

import javax.validation.bootstrap.GenericBootstrap;
import javax.validation.bootstrap.ProviderSpecificBootstrap;
import javax.validation.spi.BootstrapState;
import javax.validation.spi.ValidationProvider;

/**
 * Note: From Section 4.4.5 Validation of the 1.0 PFD Spec - Validation
 * implementations may only provide the following three public static methods:
 * buildDefaultValidatorFactory(), byDefaultProvider(), byProvider()
 * 
 * @version $Rev$ $Date$
 */
public class Validation {

    /**
     * @return ValidatorFactory
     * @throws ValidationException
     */
    public static ValidatorFactory buildDefaultValidatorFactory() {
        return byDefaultProvider().configure().buildValidatorFactory();
    }

    /**
     * @return GenericBootstrap
     */
    public static GenericBootstrap byDefaultProvider() {
        return new GenericBootstrapImpl();
    }

    /**
     * @param configurationType
     * @return ProviderSpecificBootstrap<T>
     */
    public static <T extends Configuration<T>> ProviderSpecificBootstrap<T> byProvider(
        Class<T> configurationType) {
        return new ProviderSpecificBootstrapImpl<T>(configurationType);
    }

    /*
     * (non-Javadoc) See Section 4.4.5 Validation - Must be private
     */
    private static class DefaultValidationProviderResolver implements
        ValidationProviderResolver {

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.validation.ValidationProviderResolver#getValidationProviders()
         */
        // FIXME JSR-303 - Needs to be implemented
        public List<ValidationProvider> getValidationProviders() {
            throw new ValidationException("TODO - Not implemented yet");
        }
    }

    /*
     * (non-Javadoc) See Section 4.4.5 Validation - Must be private
     */
    private static class GenericBootstrapImpl implements GenericBootstrap,
        BootstrapState {

        private ValidationProviderResolver vpResolver;

        /*
         * (non-Javadoc)
         * 
         * @see javax.validation.bootstrap.GenericBootstrap#configure()
         */
        public Configuration<?> configure() {
            ValidationProviderResolver resolv = vpResolver;
            try {
                if (resolv == null)
                    resolv = new DefaultValidationProviderResolver();
                return resolv.getValidationProviders().get(0)
                    .createGenericConfiguration(this);
            } catch (Exception e) {
                throw new ValidationException(
                    "Could not create configuration.  Nested exception = " +
                        e.getMessage());
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.validation.spi.BootstrapState#getValidationProviderResolver()
         */
        public ValidationProviderResolver getValidationProviderResolver() {
            return vpResolver;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.validation.bootstrap.GenericBootstrap#providerResolver(javax
         * .validation.ValidationProviderResolver)
         */
        public GenericBootstrap providerResolver(
            ValidationProviderResolver resolver) {
            vpResolver = resolver;
            return this;
        }
    }

    /*
     * (non-Javadoc) See Section 4.4.5 Validation - Must be private
     */
    private static class ProviderSpecificBootstrapImpl<T extends Configuration<T>>
        implements ProviderSpecificBootstrap<T> {

        private Class<T> cfgType = null;
        private ValidationProviderResolver vpResolver = null;

        /*
         * (non-Javadoc)
         * 
         * @seejavax.validation.bootstrap.ProviderSpecificBootstrap#
         * ProviderSpecificBootstrap(Class<T>)
         */
        public ProviderSpecificBootstrapImpl(Class<T> configurationType) {
            cfgType = configurationType;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.validation.bootstrap.ProviderSpecificBootstrap#configure()
         */
        public T configure() {
            T cfg = null;

            // create a default resolver if not supplied by providerResolver()
            if (vpResolver == null)
                vpResolver = new DefaultValidationProviderResolver();

            // check each provider discovered by the resolver
            for (ValidationProvider vProvider : vpResolver
                .getValidationProviders()) {
                if (vProvider.isSuitable(cfgType)) {
                    GenericBootstrapImpl bootstrap = new GenericBootstrapImpl();
                    // set the resolver
                    bootstrap.providerResolver(vpResolver);
                    // Create a Configuration<T> from the above bootstrap state
                    // and configurationType
                    cfg = vProvider.createSpecializedConfiguration(bootstrap, 
                        cfgType);
                }
            }

            // return the Configuration<T> or throw a Spec required exception
            if (cfg != null)
                return cfg;
            else
                throw new ValidationException("No provider found for " +
                    "configuration type " + cfgType);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.validation.bootstrap.ProviderSpecificBootstrap#providerResolver
         * (javax.validation.ValidationProviderResolver)
         */
        public ProviderSpecificBootstrap<T> providerResolver(
            ValidationProviderResolver resolver) {
            vpResolver = resolver;
            return this;
        }
    }
}
