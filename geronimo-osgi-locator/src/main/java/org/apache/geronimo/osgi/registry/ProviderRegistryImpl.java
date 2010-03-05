/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.osgi.registry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.osgi.framework.Bundle;
import org.osgi.service.log.LogService;

/**
 * The implementation of the provider registry used to store
 * the bundle registrations.
 */
public class ProviderRegistryImpl implements org.apache.geronimo.osgi.registry.api.ProviderRegistry {
    // our mapping between a provider id and the implementation information.  There
    // might be a one-to-many relationship between the ids and implementing classes.
    private Map<String, List<BundleProviderLoader>> providers = new HashMap<String, List<BundleProviderLoader>>();
    // our list of bundles and a mapping to the set of providers that each bundle
    // declares
    private Map<Long, List<BundleProviderLoader>> providerBundles = new HashMap<Long, List<BundleProviderLoader>>();

    // our base Activator (used as a service source)
    private Activator activator;

    public ProviderRegistryImpl(Activator activator) {
        this.activator = activator;
    }

    /**
     * Add a bundle to the provider registry.  This searches
     * for services information in the OSGI-INF/providers
     * directory of the bundle and registers this information
     * in a provider registry.  Bundles that need to locate
     * class instances can use the provider registry to
     * locate classes that might reside in other bundles.
     *
     * @param bundle The source bundle.
     *
     * @return A map of the located registrations.  Returns null if
     *         this bundle does not contain any providers.
     */
    public Object addBundle(Bundle bundle) {
        // a list for accumulating providers for this bundle
        List<BundleProviderLoader> list = new ArrayList<BundleProviderLoader>();
        // look for services definitions in the bundle...we accumulate these as provider class
        // definitions.
        Enumeration e = bundle.findEntries("OSGI-INF/providers/", "*", false);
        if (e != null) {
            while (e.hasMoreElements()) {
                final URL u = (URL) e.nextElement();
                // go parse out the control file
                parseProviderFile(u, bundle, list);
            }
        }

        // if we have providers defined, add each to the global registry and
        // also keep track of this in the bundle list
        if (!list.isEmpty()) {
            providerBundles.put(bundle.getBundleId(), list);
            // process the registrations for each item
            for (BundleProviderLoader loader: list) {
                // add to the mapping table
                register(loader);
            }
            // this will tell the tracker this is a bundle of interest.
            return list ;
        }
        else {
            // indicate our lack of interest here
            return null;
        }
    }


    /**
     * Parse a provider definition file and create loaders
     * for all definitions contained within the file.
     *
     * @param u      The URL of the file
     * @param bundle The bundle this resides in.
     * @param bundleProviders
     *               The list used to accumulate the provider definitions.
     */
    private void parseProviderFile(URL u, Bundle bundle, List<BundleProviderLoader> bundleProviders) {
        final String url = u.toString();
        // ignore directories
        if (url.endsWith("/")) {
            return;
        }
        // initial size of the provider list.  If we don't add any because of parsing the
        // lines, we can add a default one using the provider id as both key and mapped
        // provider class.
        int providerCount = bundleProviders.size();
        // the identifier used for the provider is the last item in the URL.
        final String providerId = url.substring(url.lastIndexOf("/") + 1);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream(), "UTF-8"));
            String providerClassName = null;
            // the file can be multiple lines long, with comments.  A single file can define multiple providers
            // for a single key, so we might need to create multiple entries.  If the file does not contain any
            // definition lines, then as a default, we use the providerId as an implementation class also.
            String line = br.readLine();
            while (line != null) {
                // we allow comments on these lines, and a line can be all comment
                int comment = line.indexOf('#');
                if (comment != -1) {
                    line = line.substring(0, comment);
                }
                line = line.trim();
                // if there is nothing left on the line after stripping white space and comments, skip this
                if (line.length() > 0) {
                    // add this to our list
                    bundleProviders.add(new BundleProviderLoader(providerId, line, bundle));
                }
                // keep reading until the end.
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            // ignore errors and handle as default
        }

        // if nothing was defined inside the file, then add a default
        if (providerCount == bundleProviders.size()) {
            bundleProviders.add(new BundleProviderLoader(providerId, providerId, bundle));
        }
    }


    /**
     * Remove a bundle from the registry.
     *
     * @param bundle The target bundle.
     */
    public void removeBundle(Bundle bundle) {
        List<BundleProviderLoader> list = providerBundles.remove(bundle.getBundleId());
        if (list != null) {
            for (BundleProviderLoader loader : list) {
                // unregistry the individual entry
                unregister(loader);
            }
        }
    }

    /**
     * Removed a provider registration for a named provider id.
     *
     * @param id      The target id
     * @param provider The provider registration instance
     */
    public synchronized void unregister(BundleProviderLoader provider) {
        log(LogService.LOG_DEBUG, "unregistering provider " + provider);
        if (providers != null) {
            // this is stored as a list.  Just remove using the registration information
            // This may move a different provider to the front of the list.
            List<BundleProviderLoader> l = providers.get(provider.id());
            if (l != null) {
                l.remove(provider);
            }
        }
    }


    /**
     * Register an individual provivider item by its provider identifier.
     *
     * @param id      The provider id.
     * @param provider The loader used to resolve the provider class.
     */
    public synchronized void register(BundleProviderLoader provider) {
        log(LogService.LOG_DEBUG, "registering provider " + provider);
        // if this is the first registration, create the mapping table
        if (providers == null) {
            providers = new HashMap<String, List<BundleProviderLoader>>();
        }

        String providerId = provider.id();

        // the providers are stored as a list...we use the first one registered
        // when asked to locate.
        List<BundleProviderLoader> l = providers.get(providerId);
        if (l ==  null) {
            l = new ArrayList<BundleProviderLoader>();
            providers.put(providerId, l);
        }
        l.add(provider);
    }


    /**
     * Locate a class by its provider id indicator. .
     *
     * @param providerId The provider id (generally, a fully qualified class name).
     *
     * @return The Class corresponding to this provider id.  Returns null
     *         if this is not registered or the indicated class can't be
     *         loaded.
     */
    public synchronized Class<?> locate(String providerId) {
        if (providers != null) {
            List<BundleProviderLoader> l = providers.get(providerId);
            if (l != null && !l.isEmpty()) {
                // we always use the first one registered
                BundleProviderLoader c = l.get(0);
                try {
                    // this loads the class from the hosting bundle.
                    return c.call();
                } catch (Exception e) {
                    log(LogService.LOG_DEBUG, "Exception loading provider " + this, e);
                }
            }
        }
        return null;
    }

    /**
     * Locate all class files that match a given provider id.
     *
     * @param providerId The target provider identifier.
     *
     * @return A List containing the class objects corresponding to the
     *         provider identifier.  Returns an empty list if no
     *         matching classes can be located.
     */
    public synchronized List<Class<?>> locateAll(String providerId) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        if (providers != null) {
            List<BundleProviderLoader> l = providers.get(providerId);
            if (l != null) {
                for (BundleProviderLoader c : l) {
                	try {
                    	classes.add(c.call());
                	} catch (Exception e) {
                        log(LogService.LOG_DEBUG, "Exception loading " + c, e);
                	}
				}
            }
        }
        return classes;
    }

    private void log(int level, String message) {
        activator.log(level, message);
    }

    private void log(int level, String message, Throwable th) {
        activator.log(level, message, th);
    }


    /**
     * Holder class for located services information.
     */
    private class BundleProviderLoader implements Callable<Class<?>> {
        // the class name for this provider
        private final String providerId;
        // the mapped class name of the provider.
        private final String providerClass;
        // the hosting bundle.
        private final Bundle bundle;

        /**
         * Create a loader for this registered provider.
         *
         * @param providerId The provider ID
         * @param providerClass The mapped class name of the provider.
         * @param bundle    The hosting bundle.
         */
        public BundleProviderLoader(String providerId, String providerClass, Bundle bundle) {
            this.providerId = providerId;
            this.providerClass = providerClass;
            this.bundle = bundle;
        }

        public Class<?> call() throws Exception {
            try {
                log(LogService.LOG_DEBUG, "creating provider for: " + this);
                return bundle.loadClass(providerClass);
            } catch (Exception e) {
                e.printStackTrace();
                log(LogService.LOG_DEBUG, "exception caught while creating " + this, e);
                throw e;
            } catch (Error e) {
                e.printStackTrace();
                log(LogService.LOG_DEBUG, "error caught while creating " + this, e);
                throw e;
            }
        }

        public String id() {
            return providerId;
        }

        @Override
        public String toString() {
            return "Provider interface=" + providerId + " ,provider class=" + providerClass;
        }

        @Override
        public int hashCode() {
            return providerId.hashCode() + providerClass.hashCode() + (int)bundle.getBundleId();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof BundleProviderLoader) {
                return providerId.equals(((BundleProviderLoader)obj).providerId) &&
                       providerClass.equals(((BundleProviderLoader)obj).providerClass) &&
                       bundle.getBundleId() == ((BundleProviderLoader)obj).bundle.getBundleId();
            } else {
                return false;
            }
        }
    }
}
