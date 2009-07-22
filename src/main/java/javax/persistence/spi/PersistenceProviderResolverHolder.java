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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.persistence.PersistenceException;


public class PersistenceProviderResolverHolder {

    private static PersistenceProviderResolver persistenceResolver =
        new DefaultPersistenceProviderResolver();
    
    public static PersistenceProviderResolver getPersistenceProviderResolver() {
        return persistenceResolver;
    }
    
    public static void setPersistenceProviderResolver(PersistenceProviderResolver resolver) {
        if (resolver != null) {
            if (persistenceResolver != null) {
                persistenceResolver.clearCachedProviders();
            }
            persistenceResolver = resolver;
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
        private static final Map<ClassLoader, List<PersistenceProvider>> providerCache =
            new WeakHashMap<ClassLoader, List<PersistenceProvider>>();
        
        /*
         * (non-Javadoc)
         * 
         * @see javax.persistence.spi.PersistenceProviderResolver#getPersistenceProviders()
         */
        public List<PersistenceProvider> getPersistenceProviders() {
            List<PersistenceProvider> providers;
            
            // get our class loader
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null)
                cl = DefaultPersistenceProviderResolver.class.getClassLoader();

            // use any previously cached providers
            synchronized (providerCache) {
                providers = providerCache.get(cl);
            }
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
                
                // cache the discovered providers
                synchronized (providerCache) {
                    providerCache.put(cl, providers);
                }
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
            synchronized (providerCache) {
                providerCache.clear();
            }
        }
    }

}
