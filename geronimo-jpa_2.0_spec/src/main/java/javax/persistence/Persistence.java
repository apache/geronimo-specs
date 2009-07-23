/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

//
// This source code implements specifications defined by the Java
// Community Process. In order to remain compliant with the specification
// DO NOT add / change / or delete method signatures!
//
package javax.persistence;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceProviderResolver;
import javax.persistence.spi.PersistenceProviderResolverHolder;

/**
 * @version $Rev$ $Date$
 */

/**
 * Bootstrap class that is used to obtain {@link javax.persistence.EntityManagerFactory}
 * references.
 * 
 * Contains Geronimo implemented code as required by the JPA spec.
 */
public class Persistence {

    protected static final Set<PersistenceProvider> providers = new HashSet<PersistenceProvider>();
    // Changed to the hard coded PERSISTENCE_PROVIDER value to pass signature tests.
    // public static final java.lang.String PERSISTENCE_PROVIDER = PersistenceProvider.class.getName(); 
    public static final java.lang.String PERSISTENCE_PROVIDER = "javax.persistence.spi.PeristenceProvider";
    static final String PERSISTENCE_PROVIDER_PROPERTY = "javax.persistence.provider";
    static final String PERSISTENCE_PROVIDER_SERVICE = "META-INF/services/"
            + PersistenceProvider.class.getName();

    /**
     * Create and return an EntityManagerFactory for the named persistence unit.
     *
     * @param persistenceUnitName The name of the persistence unit
     * @return The factory that creates EntityManagers configured according to the
     *         specified persistence unit
     */
    public static EntityManagerFactory createEntityManagerFactory(
            String persistenceUnitName) {
        return createEntityManagerFactory(persistenceUnitName, Collections.EMPTY_MAP);
    }

    /**
     * Create and return an EntityManagerFactory for the named persistence unit using the
     * given properties.
     *
     * @param persistenceUnitName The name of the persistence unit
     * @param properties          Additional properties to use when creating the factory. The values of
     *                            these properties override any values that may have been configured
     *                            elsewhere.
     * @return The factory that creates EntityManagers configured according to the
     *         specified persistence unit.
     */
    public static EntityManagerFactory createEntityManagerFactory(
            String persistenceUnitName, Map properties) {
        EntityManagerFactory factory = null;
        if (properties == null) {
            properties = Collections.EMPTY_MAP;
        }

        /*
         * Geronimo/OpenJPA unique behavior - Start by loading a provider
         * explicitly specified in properties. The spec doesn't seem to forbid
         *  providers that are not deployed as a service.
         */
        Object providerName = properties.get(PERSISTENCE_PROVIDER_PROPERTY);
        if (providerName instanceof String) {
            factory = createFactory(
                    providerName.toString(),
                    persistenceUnitName,
                    properties);
        }

        if (factory == null) {
            /*
             * Now, the default behavior of loading a provider from our resolver
             */
            PersistenceProviderResolver resolver =
                PersistenceProviderResolverHolder.getPersistenceProviderResolver();
            List<PersistenceProvider> providers = resolver.getPersistenceProviders();
            for (PersistenceProvider provider : providers) {
                factory = provider.createEntityManagerFactory(
                    persistenceUnitName, properties);
                if (factory != null) {
                    break;
                }
            }
        }

        // spec doesn't mention any exceptions thrown by this method if no emf
        return factory;
    }

    /*
     * Geronimo/OpenJPA private helper code for PERSISTENCE_PROVIDER_PROPERTY
     */
    private static EntityManagerFactory createFactory(
            String providerName,
            String persistenceUnitName,
            Map properties)
            throws PersistenceException {

        Class<?> providerClass;
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null)
            cl = Persistence.class.getClassLoader();
        try {
            providerClass = Class.forName(providerName, true, cl);
        } catch (Exception e) {
            throw new PersistenceException("Invalid or inaccessible provider class: " + providerName, e);
        }
        try {
            PersistenceProvider provider = (PersistenceProvider) providerClass.newInstance();
            return provider.createEntityManagerFactory(persistenceUnitName, properties);
        } catch (Exception e) {
            throw new PersistenceException("Provider error. Provider: " + providerName, e);
        }
    }
    
    /*
    * @return PersistenceUtil instance
    * @since 2.0
    */
    public static PersistenceUtil getPersistenceUtil() {
        return new PersistenceUtilImpl();
    }
    
    /*
     * Geronimo implementation specific code
     */
    private static class PersistenceUtilImpl implements PersistenceUtil {

            public boolean isLoaded(Object entity, String attributeName) {
                // TODO Auto-generated dummy method stub
                return true;
            }

            public boolean isLoaded(Object entity) {
                // TODO Auto-generated dummy method stub
                return true;
            }
    }
    
}
