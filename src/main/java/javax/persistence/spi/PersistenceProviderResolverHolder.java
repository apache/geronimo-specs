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

package javax.persistence.spi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.WeakHashMap;

import javax.persistence.PersistenceException;

import org.apache.geronimo.osgi.locator.ProviderLocator;

/**
 * Contains Geronimo implemented code as required by the JPA spec.
 *
 * Finds/Creates the global {@link javax.persistence.spi.PersistenceProviderResolver}
 *
 * Implementations must be thread-safe.
 *
 * @since Java Persistence 2.0
 */
public class PersistenceProviderResolverHolder {

    private static PersistenceProviderResolver persistenceResolver =
        new DefaultPersistenceProviderResolver();

    public static PersistenceProviderResolver getPersistenceProviderResolver() {
        return persistenceResolver;
    }

    public static void setPersistenceProviderResolver(PersistenceProviderResolver resolver) {
        if (persistenceResolver != null) {
            persistenceResolver.clearCachedProviders();
            persistenceResolver = null;
        }
        if (resolver != null) {
            persistenceResolver = resolver;
        } else {
            // handle removing a resolver for OSGi environments
            persistenceResolver = new DefaultPersistenceProviderResolver();
        }
    }

    /*
     * (non-Javadoc) Default implementation of a PersistenceProviderResolver
     * to use when none are provided.
     *
     * Geronimo implementation specific code.
     */
    private static class DefaultPersistenceProviderResolver implements PersistenceProviderResolver {

        private static final String SERVICES_FILENAME = "META-INF/services/" +
            PersistenceProvider.class.getName();

        // cache of providers per class loader
        private volatile WeakHashMap<ClassLoader, List<PersistenceProvider>> providerCache =
            new WeakHashMap<ClassLoader, List<PersistenceProvider>>();

        /*
         * (non-Javadoc)
         *
         * @see javax.persistence.spi.PersistenceProviderResolver#getPersistenceProviders()
         */
        public List<PersistenceProvider> getPersistenceProviders() {
            // get our class loader
            ClassLoader cl = PrivClassLoader.get(null);
            if (cl == null)
                cl = PrivClassLoader.get(DefaultPersistenceProviderResolver.class);

            // use any previously cached providers
            List<PersistenceProvider> providers = providerCache.get(cl);
            if (providers == null) {
                // need to discover and load them for this class loader
                providers = new ArrayList<PersistenceProvider>();
                try {
                    // find all service provider files
                    Enumeration<URL> cfgs = cl.getResources(SERVICES_FILENAME);
                    while (cfgs.hasMoreElements()) {
                        URL url = cfgs.nextElement();
                        InputStream is = null;
                        try {
                            is = url.openStream();
                            BufferedReader br = new BufferedReader(
                                new InputStreamReader(is, "UTF-8"), 256);
                            String line = br.readLine();
                            // files may contain multiple providers and/or comments
                            while (line != null) {
                                line = line.trim();
                                if (!line.startsWith("#")) {
                                    try {
                                        // try loading the specified class
                                        final Class<?> provider = cl.loadClass(line);
                                        // create an instance to return
                                        providers.add((PersistenceProvider) provider.newInstance());
                                    } catch (ClassNotFoundException e) {
                                        throw new PersistenceException("Failed to load provider " +
                                            line + " configured in file " + url, e);
                                    } catch (InstantiationException e) {
                                        throw new PersistenceException("Failed to instantiate provider " +
                                            line + " configured in file " + url, e);
                                    } catch (IllegalAccessException e) {
                                        throw new PersistenceException("Failed to access provider " +
                                            line + " configured in file " + url, e);
                                    }
                                }
                                line = br.readLine();
                            }
                            is.close();
                            is = null;
                        } catch (IOException e) {
                            throw new PersistenceException("Error trying to read " + url, e);
                        } finally {
                            if (is != null)
                                is.close();
                        }
                    }
                } catch (IOException e) {
                    throw new PersistenceException("Error trying to load " + SERVICES_FILENAME, e);
                }

                try {

                    // if we're running in an OSGi environment, there might be additional reqistered
                    // persistence providers.   Process them also
                    List<Class<?>> osgiProviders = ProviderLocator.locateAll(PersistenceProvider.class.getName());

                    for (Class<?> provider : osgiProviders) {
                        // add this instance to the cache of providers
                        providers.add((PersistenceProvider) provider.newInstance());
                    }
                } catch (InstantiationException e) {
                    throw new PersistenceException("Failed to instantiate provider osgi provider", e);
                } catch (IllegalAccessException e) {
                    throw new PersistenceException("Failed to access osgi provider", e);
                } catch (ClassCastException e) {
                    throw new PersistenceException("Invalid osgi provider definition", e);
                }

                // cache the discovered providers
                providerCache.put(cl, providers);
            }

            // caller must handle the case of no providers found
            return providers;
        }

        /*
         * (non-Javadoc)
         *
         * @see javax.persistence.spi.PersistenceProviderResolver#clearCachedProviders()
         */
        public void clearCachedProviders() {
            providerCache.clear();
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

