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

import javax.persistence.spi.LoadState;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceProviderResolver;
import javax.persistence.spi.PersistenceProviderResolverHolder;
import javax.persistence.spi.ProviderUtil;

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
     * @param persistenceUnitName Name of the persistence unit
     * @return The factory for the specified persistence unit or null if none
     *         are applicable.
     */
    public static EntityManagerFactory createEntityManagerFactory(
            String persistenceUnitName) {
        return createEntityManagerFactory(persistenceUnitName, Collections.EMPTY_MAP);
    }

    /**
     * Create and return an EntityManagerFactory for the named persistence unit
     * using the given properties.
     *
     * @param persistenceUnitName Name of the persistence unit
     * @param properties Additional properties to use when creating the
     *                   persistence unit factory. These properties override any
     *                   values that have been configured elsewhere.
     * @return The factory for the specified persistence unit or null if none
     *         are applicable.
     */
    public static EntityManagerFactory createEntityManagerFactory(
            String persistenceUnitName, Map properties) {
        
        EntityManagerFactory factory = null;
        
        if (properties == null) {
            properties = Collections.EMPTY_MAP;
        }

        // get the discovered set of providers
        PersistenceProviderResolver resolver =
            PersistenceProviderResolverHolder.getPersistenceProviderResolver();
        // following will throw PersistenceExceptions for invalid services
        List<PersistenceProvider> providers = resolver.getPersistenceProviders();

        /*
         * Geronimo/OpenJPA 1.0 unique behavior - Start by loading a provider
         * explicitly specified in properties and return any exceptions. The
         * spec doesn't forbid providers that aren't a service - it only states
         * that they "should" be implemented as one in Sect. 9.2.
         * 
         * For 2.0 - We only perform the above behavior if the specified
         * provider is not in the discovered list.
         * 
         * Note: This special non-spec defined case will rethrow any encountered
         * Exceptions as a PersistenceException.
         */
        Object providerName = properties.get(PERSISTENCE_PROVIDER_PROPERTY);
        if ((providerName != null) && (providerName instanceof String)) {
            boolean isLoaded = false;
            // search the discovered providers for this explicit provider
            for (PersistenceProvider provider : providers) {
                if (provider.getClass().getName().compareTo(providerName.toString()) == 0) {
                    isLoaded = true;
                    break;
                }
            }
            /*
             * Only try to explicitly create this provider if we didn't
             * find it as a service, while rethrowing any Exceptions to
             * match the old 1.0 behavior
             */
            if (!isLoaded) {
                factory = createFactory(
                    providerName.toString(),
                    persistenceUnitName,
                    properties);
                if (factory != null) {
                    return factory;
                }
            }
        }
        
        // Now, the default behavior of loading a provider from our resolver
        for (PersistenceProvider provider : providers) {
            try {
                factory = provider.createEntityManagerFactory(
                    persistenceUnitName, properties);                    
            } catch (Exception e) {
                /*
                 * Note:  Change in behavior from 1.0 -
                 *   Spec states that a provider "must" return null if it
                 *   cannot fulfill an EMF request, so ignore any exceptions
                 *   that are thrown if we have more than one provider,
                 *   so the other providers have a chance to return an EMF.
                 */
                if (providers.size() == 1)
                {
                    // this is the only provider, so rethrow exception
                    throw new PersistenceException(e);
                }
            }
            if (factory != null) {
                return factory;
            }
        }

        // spec doesn't mention any exceptions thrown by this method if no emf
        // returned, but old 1.0 behavior always generated an EMF or exception
        throw new PersistenceException("No Persistence providers found for PU=" + persistenceUnitName);
    }

    /*
     * Geronimo/OpenJPA private helper code for PERSISTENCE_PROVIDER_PROPERTY
     * @return EntityManagerFactory or null
     * @throws PersistenceException
     */
    private static EntityManagerFactory createFactory(String providerName,
            String persistenceUnitName, Map properties)
            throws PersistenceException {

        Class<?> providerClass;
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null)
            cl = Persistence.class.getClassLoader();
        try {
            providerClass = Class.forName(providerName, true, cl);
        } catch (Exception e) {
            throw new PersistenceException("Invalid or inaccessible explicit provider class: " +
                providerName, e);
        }
        try {
            PersistenceProvider provider = (PersistenceProvider) providerClass.newInstance();
            return provider.createEntityManagerFactory(persistenceUnitName, properties);
        } catch (Exception e) {
            throw new PersistenceException("Explicit error returned from provider: " +
                providerName, e);
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

        /*
         * Determines the load state of the attribute of an entity 
         * @see javax.persistence.PersistenceUtil#isLoaded(java.lang.Object, java.lang.String)
         */
            public boolean isLoaded(Object entity, String attributeName) {
                boolean isLoaded = true;

                // Get the list of persistence providers from the resolver
                PersistenceProviderResolver ppr = 
                    PersistenceProviderResolverHolder.getPersistenceProviderResolver();
                List<PersistenceProvider> pps = ppr.getPersistenceProviders();

                // Iterate through the list using ProviderUtil.isLoadedWithoutReference()
                for (PersistenceProvider pp : pps) {
                    try {
                        ProviderUtil pu = pp.getProviderUtil();                        
                        LoadState ls = pu.isLoadedWithoutReference(entity, attributeName);
                        if (ls == LoadState.LOADED)
                            return true;
                        if (ls == LoadState.NOT_LOADED)
                            return false;
                    }
                    catch (Throwable t) {
                        // JPA 1.0 providers will not implement the getProviderUtil
                        // method.  Eat the exception and try the next provider.
                    }
                 }
                // Iterate through the list a second time using ProviderUtil.isLoadedWithReference()
                for (PersistenceProvider pp : pps) {
                    try {
                        ProviderUtil pu = pp.getProviderUtil();                        
                        LoadState ls = pu.isLoadedWithoutReference(entity, attributeName);
                        if (ls == LoadState.LOADED)
                            return true;
                        if (ls == LoadState.NOT_LOADED)
                            return false;
                    }
                    catch (Throwable t) {
                        // JPA 1.0 providers will not implement the getProviderUtil
                        // method.  Eat the exception and try the next provider.
                    }
                 }
                
                // All providers returned a load state of unknown.  Return true.
                return true;
            }

            public boolean isLoaded(Object entity) {
                // Get the list of persistence providers from the resolver
                PersistenceProviderResolver ppr = 
                    PersistenceProviderResolverHolder.getPersistenceProviderResolver();
                List<PersistenceProvider> pps = ppr.getPersistenceProviders();

                // Iterate through the list of providers, using ProviderUtil to
                // determine the load state
                for (PersistenceProvider pp : pps) {
                    try {
                        ProviderUtil pu = pp.getProviderUtil();
                        LoadState ls = pu.isLoaded(entity);
                        if (ls == LoadState.LOADED)
                            return true;
                        if (ls == LoadState.NOT_LOADED)
                            return false;
                        // Otherwise, load state is unknown.  Query the next provider.
                    }
                    catch (Throwable t) {
                        // JPA 1.0 providers will not implement the getProviderUtil
                        // method.  Eat the exception and try the next provider.
                    }                   
                }
                
                // All providers returned a load state of unknown.  Return true.
                return true;
            }
    }
    
}
