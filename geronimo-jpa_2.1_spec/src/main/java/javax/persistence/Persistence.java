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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.spi.LoadState;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceProviderResolver;
import javax.persistence.spi.PersistenceProviderResolverHolder;
import javax.persistence.spi.ProviderUtil;

import org.apache.geronimo.osgi.locator.ProviderLocator;

/**
 * Bootstrap class to obtain {@link javax.persistence.EntityManagerFactory}
 * references.
 *
 * Contains Geronimo implemented code as required by the JPA spec.
 *
 * @version $Rev$ $Date$
 */
public class Persistence {

    // The following variable is only here for TCK backward compatibility
    @Deprecated
    protected static final Set<PersistenceProvider> providers = new HashSet<PersistenceProvider>();

    // The following string is only here for TCK backward compatibility
    @Deprecated
    public static final String PERSISTENCE_PROVIDER = "javax.persistence.spi.PeristenceProvider";

    static final String PERSISTENCE_PROVIDER_PROPERTY = "javax.persistence.provider";

    static final String PERSISTENCE_PROVIDER_SERVICE = "META-INF/services/"
            + PersistenceProvider.class.getName();


    public static EntityManagerFactory createEntityManagerFactory(
            String persistenceUnitName) {
        return createEntityManagerFactory(persistenceUnitName, Collections.EMPTY_MAP);
    }

    /**
     * Geronimo implementation specific code
     */
    public static EntityManagerFactory createEntityManagerFactory(
            String persistenceUnitName, Map properties) {

        EntityManagerFactory factory = null;
        Map props = properties;
        if (props == null) {
            props = Collections.EMPTY_MAP;
        }

        // get the discovered set of providers
        List<PersistenceProvider> providers = getProviders();

        /*
         * Geronimo/OpenJPA 1.0 unique behavior - Start by loading a provider
         * explicitly specified in the properties and return any exceptions.
         * The spec doesn't forbid providers that aren't a service - it only
         * states that they "should" be implemented as services in Sect. 9.2.
         *
         * For 2.0 - We only perform the above behavior if the specified
         * provider is not in the discovered list.
         *
         * Note: This special non-spec defined case will rethrow any encountered
         * Exceptions as a PersistenceException.
         */
        Object propVal = props.get(PERSISTENCE_PROVIDER_PROPERTY);
        if ((propVal != null) && (propVal instanceof String)) {
            boolean isLoaded = false;
            String providerName = propVal.toString();
            // search the discovered providers for this explicit provider
            for (PersistenceProvider provider : providers) {
                if (provider.getClass().getName().compareTo(providerName) == 0) {
                    isLoaded = true;
                    break;
                }
            }
            /*
             * Only try to explicitly create this provider if we didn't
             * find it as a service, while rethrowing any exceptions to
             * match the old 1.0 behavior
             */
            if (!isLoaded) {
                factory = createFactory(
                    providerName.toString(),
                    persistenceUnitName,
                    props);
                if (factory != null) {
                    return factory;
                }
            }
        }

        /*
         * Now, the default JPA2 behavior of loading a provider from our resolver
         *
         * Note:  Change in behavior from 1.0, which always returned exceptions:
         *   Spec states that a provider "must" return null if it
         *   cannot fulfill an EMF request, so that if we have more than one
         *   provider, then the other providers have a chance to return an EMF.
         *   Now, we will return any exceptions wrapped in a
         *   PersistenceException to match 1.0 behavior and provide more
         *   diagnostics to the end user.
         */

        // capture any provider returned exceptions
        Map<String, Throwable> exceptions = new HashMap<String, Throwable>();
        // capture the provider names to use in the exception text if needed
        StringBuffer foundProviders = null;

        for (PersistenceProvider provider : providers) {
            String providerName = provider.getClass().getName();
            try {
                factory = provider.createEntityManagerFactory(persistenceUnitName, props);
            } catch (Exception e) {
                // capture the exception details and give other providers a chance
                exceptions.put(providerName, e);
            }
            if (factory != null) {
                // we're done
                return factory;
            } else {
                // update the list of providers we have tried
                if (foundProviders == null) {
                    foundProviders = new StringBuffer(providerName);
                } else {
                    foundProviders.append(", ");
                    foundProviders.append(providerName);
                }
            }
        }

        // make sure our providers list is initialized for the exceptions below
        if (foundProviders == null) {
            foundProviders = new StringBuffer("NONE");
        }

        /*
         * Spec doesn't mention any exceptions thrown by this method if no emf
         * returned, but old 1.0 behavior always generated an EMF or exception.
         */
        if (exceptions.isEmpty()) {
            // throw an exception with the PU name and providers we tried
            throw new PersistenceException("No persistence providers available for \"" + persistenceUnitName +
                "\" after trying the following discovered implementations: " + foundProviders);
        } else {
            // we encountered one or more exceptions, so format and throw as a single exception
            throw createPersistenceException(
                "Explicit persistence provider error(s) occurred for \"" + persistenceUnitName +
                "\" after trying the following discovered implementations: " + foundProviders,
                exceptions);
        }
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

        // get our class loader
        ClassLoader cl = PrivClassLoader.get(null);
        if (cl == null)
            cl = PrivClassLoader.get(Persistence.class);

        try {
            providerClass = ProviderLocator.loadClass(providerName, Persistence.class, cl);
        } catch (Exception e) {
            throw new PersistenceException("Invalid or inaccessible explicit provider class: " +
                providerName, e);
        }
        try {
            PersistenceProvider provider = (PersistenceProvider) providerClass.newInstance();
            return provider.createEntityManagerFactory(persistenceUnitName, properties);
        } catch (Exception e) {
            throw new PersistenceException("Explicit error returned from provider: " +
                providerName + " for PU: " + persistenceUnitName, e);
        }
    }


    /**
     * Geronimo/OpenJPA private helper code for creating a PersistenceException
     * @param msg String to use as the exception message
     * @param failures Persistence provider exceptions to add to the exception message
     * @return PersistenceException
     */
    private static PersistenceException createPersistenceException(String msg, Map<String, Throwable> failures) {
        String newline = System.getProperty("line.separator");
        StringWriter strWriter = new StringWriter();
        strWriter.append(msg);
        if (failures.size() <= 1) {
            // we caught an exception, so include it as the cause
            Throwable t = null;
            for (String providerName : failures.keySet()) {
                t = failures.get(providerName);
                strWriter.append(" from provider: ");
                strWriter.append(providerName);
                break;
            }
            return new PersistenceException(strWriter.toString(), t);
        } else {
            // we caught multiple exceptions, so format them into the message string and don't set a cause
            strWriter.append(" with the following failures:");
            strWriter.append(newline);
            for (String providerName : failures.keySet()) {
                strWriter.append(providerName);
                strWriter.append(" returned: ");
                failures.get(providerName).printStackTrace(new PrintWriter(strWriter));
            }
            strWriter.append(newline);
            return new PersistenceException(strWriter.toString());
        }
    }

    public static PersistenceUtil getPersistenceUtil() {
        return new PersistenceUtilImpl();
    }

    public static void generateSchema(String persistenceUnitName, Map properties) {
        final List<PersistenceProvider> providers = getProviders();
        for (final PersistenceProvider provider : providers) {
            if (provider.generateSchema( persistenceUnitName, properties)) {
                return;
            }
        }
        throw new PersistenceException("No provider for schema generation of unit '" + persistenceUnitName + "'");
    }

    private static List<PersistenceProvider> getProviders() {
        // get the discovered set of providers
        PersistenceProviderResolver resolver =
                PersistenceProviderResolverHolder.getPersistenceProviderResolver();
        // following will throw PersistenceExceptions for invalid services
        return resolver.getPersistenceProviders();
    }

    /**
     * Geronimo implementation specific code
     */
    private static class PersistenceUtilImpl implements PersistenceUtil {

        /**
         * Determines the load state of the attribute of an entity
         * @see javax.persistence.PersistenceUtil#isLoaded(java.lang.Object, java.lang.String)
         */
            public boolean isLoaded(Object entity, String attributeName) {
                boolean isLoaded = true;

                // Get the list of persistence providers from the resolver
                List<PersistenceProvider> pps = getProviders();

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
                        LoadState ls = pu.isLoadedWithReference(entity, attributeName);
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
                List<PersistenceProvider> pps = getProviders();

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

    /**
     * Geronimo/OpenJPA private helper code for handling class loaders
     */
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

