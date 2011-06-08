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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.service.log.LogService;

/**
 * The implementation of the provider registry used to store
 * the bundle registrations.
 */
public class ProviderRegistryImpl implements org.apache.geronimo.osgi.registry.api.ProviderRegistry {
    // indicates a bundle wishes to opt in to the META-INF/services registration and tracking.
    public static final String OPT_IN_HEADER = "SPI-Provider";
    // provider classes exported via a header.
    public static final String EXPORT_PROVIDER_HEADER = "Export-SPI-Provider";
    // our mapping between a provider id and the implementation information.  There
    // might be a one-to-many relationship between the ids and implementing classes.
    private SPIRegistry providers = new SPIRegistry();
    // our mapping between an interface name and a META-INF/services SPI implementation.  There
    // might be a one-to-many relationship between the ids and implementing classes.
    private SPIRegistry serviceProviders = new SPIRegistry();

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
        log(LogService.LOG_DEBUG, "adding bundle " + bundle);
        // create a tracker item for this bundle.  This will record all of the information
        // that's relevent to this bundle
        BundleResources tracker = new BundleResources(bundle);

        // if the tracker found information of interest, return it to the
        // BundleTracker to let it know we need to watch this one.
        return tracker.needsTracking() ? tracker : null;
    }


    /**
     * Remove a bundle from the registry.
     *
     * @param bundle The target bundle.
     */
    public void removeBundle(Bundle bundle, Object obj) {
        log(LogService.LOG_DEBUG, "removing bundle " + bundle);
        BundleResources tracker = (BundleResources)obj;
        if (tracker != null) {
            tracker.remove();
        }
    }


    /**
     * Register an individual provivider item by its provider identifier.
     *
     * @param id      The provider id.
     * @param provider The loader used to resolve the provider class.
     */
    protected void registerProvider(BundleProviderLoader provider) {
        log(LogService.LOG_DEBUG, "registering provider " + provider);
        providers.register(provider);
    }

    /**
     * Removed a provider registration for a named provider id.
     *
     * @param id      The target id
     * @param provider The provider registration instance
     */
    protected void unregisterProvider(BundleProviderLoader provider) {
        log(LogService.LOG_DEBUG, "unregistering provider " + provider);
        providers.unregister(provider);
    }


    /**
     * Register an individual provivider item by its provider identifier.
     *
     * @param id      The provider id.
     * @param provider The loader used to resolve the provider class.
     */
    protected void registerService(BundleProviderLoader provider) {
        log(LogService.LOG_DEBUG, "registering service " + provider);
        serviceProviders.register(provider);
    }

    /**
     * Removed a provider registration for a named provider id.
     *
     * @param id      The target id
     * @param provider The provider registration instance
     */
    protected void unregisterService(BundleProviderLoader provider) {
        log(LogService.LOG_DEBUG, "unregistering service " + provider);
        serviceProviders.unregister(provider);
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
    public Class<?> locate(String providerId) {
        // see if we have a registered match for this...getting just the first instance
        BundleProviderLoader loader = providers.getLoader(providerId);
        if (loader != null) {
            try {
                // try to load this.  We always return null
                return loader.loadClass();
            } catch (Exception e) {
                e.printStackTrace();
                // just swallow this and return null.  The exception has already
                // been logged.
            }
        }
        // no match to return
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
    public List<Class<?>> locateAll(String providerId) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        List<BundleProviderLoader> l = providers.getLoaders(providerId);
        // this returns null if nothing is found.
        if (l != null) {
            for (BundleProviderLoader c : l) {
                try {
                    classes.add(c.loadClass());
                } catch (Exception e) {
                    // just swallow this and proceed to the next.  The exception has
                    // already been logged.
                }
            }
        }
        return classes;
    }


    /**
     * Locate and instantiate an instance of a service provider
     * defined in the META-INF/services directory of tracked bundles.
     *
     * @param providerId The name of the target interface class.
     *
     * @return The service instance.  Returns null if no service defintions
     *         can be located.
     * @exception Exception Any classloading or other exceptions thrown during
     *                      the process of creating this service instance.
     */
    public Object getService(String providerId) throws Exception {
        List<BundleProviderLoader> loaders = serviceProviders.getLoaders(providerId);
        if (loaders == null || loaders.size() == 0) {
            return null;
        }
        String preferenceProviderClassName = System.getProperty(providerId);
        if (preferenceProviderClassName != null) {
            for (BundleProviderLoader loader : loaders) {
                if (loader.providerClass.equals(preferenceProviderClassName)) {
                    return loader.createInstance();
                }
            }
        }
        return loaders.get(0).createInstance();
    }

    /**
     * Locate all services that match a given provider id and create instances.
     *
     * @param providerId The target provider identifier.
     *
     * @return A List containing the instances corresponding to the
     *         provider identifier.  Returns an empty list if no
     *         matching classes can be located or created
     */
    public List<Object> getServices(String providerId) {
        List<Object> instances = new ArrayList<Object>();
        List<BundleProviderLoader> l = serviceProviders.getLoaders(providerId);
        // this returns null for nothing found
        if (l != null) {
            for (BundleProviderLoader c : l) {
                try {
                    instances.add(c.createInstance());
                } catch (Exception e) {
                    // just swallow this and proceed to the next.  The exception has
                    // already been logged.
                }
            }
        }
        return instances;
    }


    /**
     * Locate all services that match a given provider id and return the implementation
     * classes
     *
     * @param providerId The target provider identifier.
     *
     * @return A List containing the classes corresponding to the
     *         provider identifier.  Returns an empty list if no
     *         matching classes can be located.
     */
    public List<Class<?>> getServiceClasses(String providerId) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        List<BundleProviderLoader> l = serviceProviders.getLoaders(providerId);
        // this returns null for nothing found
        if (l != null) {
            for (BundleProviderLoader c : l) {
                try {
                    classes.add(c.loadClass());
                } catch (Exception e) {
                    e.printStackTrace();
                    // just swallow this and proceed to the next.  The exception has
                    // already been logged.
                }
            }
        }
        return classes;
    }


    /**
     * Locate and return the class for a service provider
     * defined in the META-INF/services directory of tracked bundles.
     *
     * @param providerId The name of the target interface class.
     *
     * @return The provider class.   Returns null if no service defintions
     *         can be located.
     * @exception Exception Any classloading or other exceptions thrown during
     *                      the process of loading this service provider class.
     */
    public Class<?> getServiceClass(String providerId) throws ClassNotFoundException {
        List<BundleProviderLoader> loaders = serviceProviders.getLoaders(providerId);
        if (loaders == null || loaders.size() == 0) {
            return null;
        }
        String preferenceProviderClassName = System.getProperty(providerId);
        if (preferenceProviderClassName != null) {
            for (BundleProviderLoader loader : loaders) {
                if (loader.providerClass.equals(preferenceProviderClassName)) {
                    return loader.loadClass();
                }
            }
        }
        return loaders.get(0).loadClass();
    }

    private void log(int level, String message) {
        activator.log(level, message);
    }

    private void log(int level, String message, Throwable th) {
        activator.log(level, message, th);
    }


    private class BundleResources {
        // the bundle we're attached to.
        private Bundle bundle;
        // our map of providers maintained for the META-INF/services design pattern.
        // this is an interface-to-provider instance mapping.
        private List<BundleProviderLoader> serviceProviders;
        // the defined mapping for provider classes...not maintained as an
        // interface-to-provider mapping.
        private List<BundleProviderLoader> providers;

        public BundleResources(Bundle b) {
            bundle = b;
            // go locate any services we need
            locateProviders();
            locateServices();
        }

        public boolean needsTracking() {
            return serviceProviders != null || providers != null;
        }

        // locate and process any providers defined in the OSGI-INF/providers directory
        private void locateProviders() {
            // we accumulate from the headers and the providers directory.  The headers
            // are simpler if there is no class mapping and is easier to use when
            // converting a simple jar to a bundle.
            Set<BundleProviderLoader> locatedProviders = new LinkedHashSet<BundleProviderLoader>();
            List<BundleProviderLoader> headerProviders = locateHeaderProviderDefinitions();
            if (headerProviders != null) {
                locatedProviders.addAll(headerProviders);
            }

            List<BundleProviderLoader> directoryProviders = processDefinitions("OSGI-INF/providers/");
            if (directoryProviders != null) {
                locatedProviders.addAll(directoryProviders);
            }
            // if we have anything, add to global registry
            if (!locatedProviders.isEmpty()) {
                // process the registrations for each item
                for (BundleProviderLoader loader: locatedProviders) {
                    // add to the mapping table
                    registerProvider(loader);
                }
                // remember this list so we can unregister when the bundle is stopped
                providers = new ArrayList<BundleProviderLoader>(locatedProviders);
            }
        }

        /**
         * Parse the Export-Provider: header to create a list of
         * providers that are exported via the header syntax
         * rather than via a provider mapping file.
         *
         * @return A list of providers defined on the header, or null if
         *         no providers were exported.
         */
        private List<BundleProviderLoader> locateHeaderProviderDefinitions() {
            // check the header to see if there's anything defined here.
            String exportedProviders = (String)bundle.getHeaders().get(EXPORT_PROVIDER_HEADER);
            if (exportedProviders == null) {
                return null;
            }

            List<BundleProviderLoader>providers = new ArrayList<BundleProviderLoader>();
            // split on the separator
            String[] classNames = exportedProviders.split(",");

            for (String name : classNames) {
                name = name.trim();
                // this is a simple mapping
                providers.add(new BundleProviderLoader(name, name, bundle));
            }
            return providers;
        }

        // now process any services
        private void locateServices() {
            // we only process these if there is a header indicating this
            // bundle wants to opt-in to this registration process.
            if (bundle.getHeaders().get(OPT_IN_HEADER) == null) {
                return;
            }

            log(LogService.LOG_INFO, OPT_IN_HEADER + " Manifest header found in bundle: " + bundle.getSymbolicName());

            serviceProviders = processDefinitions("META-INF/services/");
            // if we have anything, add to global registry
            if (serviceProviders != null) {
                // process the registrations for each item
                for (BundleProviderLoader loader: serviceProviders) {
                    // add to the mapping table
                    registerService(loader);
                }
            }
        }


        /**
         * Remove all resources associated with this bundle from the
         * global registry.
         */
        public void remove() {
            log(LogService.LOG_DEBUG, "removing bundle " + bundle);
            if (providers != null) {
                for (BundleProviderLoader loader : providers) {
                    // unregistry the individual entry
                    unregisterProvider(loader);
                }
            }

            if (serviceProviders != null) {
                for (BundleProviderLoader loader : serviceProviders) {
                    // unregistry the individual entry
                    unregisterService(loader);
                }
            }
        }


        /**
         * Process all of the service definition files in a given
         * target path.  This is used to process both the
         * META-INF/services files and the OSGI-INF/providers files.
         *
         * @param path   The target path location.
         *
         * @return The list of matching service definitions.  Returns null if
         *         no matches were found.
         */
        private List<BundleProviderLoader> processDefinitions(String path) {
            List<BundleProviderLoader> mappings = new ArrayList<BundleProviderLoader>();

            // look for services definitions in the bundle...we accumulate these as provider class
            // definitions.
            @SuppressWarnings("unchecked")
            Enumeration<URL> e = bundle.findEntries(path, "*", false);
            if (e != null) {
                while (e.hasMoreElements()) {
                    final URL u = e.nextElement();
                    // go parse out the control file
                    parseServiceFile(u, mappings);
                }
            }
            // only return this if we have something associated with this bundle
            return mappings.isEmpty() ? null : mappings;
        }


        /**
         * Parse a provider definition file and create loaders
         * for all definitions contained within the file.
         *
         * @param u      The URL of the file
         *
         * @return A list of the defined mappings.
         */
        private void parseServiceFile(URL u, List<BundleProviderLoader>mappings) {
            final String url = u.toString();
            // ignore directories
            if (url.endsWith("/")) {
                return;
            }

            // the identifier used for the provider is the last item in the URL.
            final String providerId = url.substring(url.lastIndexOf("/") + 1);
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream(), "UTF-8"));
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
                        mappings.add(new BundleProviderLoader(providerId, line, bundle));
                    }
                    // keep reading until the end.
                    line = br.readLine();
                }
                br.close();
            } catch (IOException e) {
                // ignore errors and handle as default
            }
        }
    }


    /**
     * Holder class for information about a given collection of
     * id to provider mappings.  Used for both the providers and
     * the services.
     */
    private class SPIRegistry {
        private Map<String, List<BundleProviderLoader>> registry;


        /**
         * Register an individual provivider item by its provider identifier.
         *
         * @param id      The provider id.
         * @param provider The loader used to resolve the provider class.
         */
        public synchronized void register(BundleProviderLoader provider) {
            // if this is the first registration, create the mapping table
            if (registry == null) {
                registry = new HashMap<String, List<BundleProviderLoader>>();
            }

            String providerId = provider.id();

            // the providers are stored as a list...we use the first one registered
            // when asked to locate.
            List<BundleProviderLoader> l = registry.get(providerId);
            if (l ==  null) {
                l = new ArrayList<BundleProviderLoader>();
                registry.put(providerId, l);
            }
            l.add(provider);
        }

        /**
         * Remove a provider registration for a named provider id.
         *
         * @param provider The provider registration instance
         */
        public synchronized void unregister(BundleProviderLoader provider) {
            if (registry != null) {
                // this is stored as a list.  Just remove using the registration information
                // This may move a different provider to the front of the list.
                List<BundleProviderLoader> l = registry.get(provider.id());
                if (l != null) {
                    l.remove(provider);
                }
            }
        }


        private synchronized BundleProviderLoader getLoader(String id) {
            // synchronize on the registry instance
            if (registry != null) {
                // return the first match, if any
                List<BundleProviderLoader> list = registry.get(id);
                if (list != null && !list.isEmpty()) {
                    return list.get(0);
                }
            }
            // no match here
            return null;
        }


        private synchronized List<BundleProviderLoader> getLoaders(String id) {
            if (registry != null) {
                // if we have matches, return a copy of what we currently have
                // to create a safe local copy.
                List<BundleProviderLoader> list = registry.get(id);
                if (list != null && !list.isEmpty()) {
                    return new ArrayList<BundleProviderLoader>(list);
                }
            }
            // no match here
            return null;
        }
    }


    /**
     * Holder class for located services information.
     */
    private class BundleProviderLoader {
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

        /**
         * Load a provider class.
         *
         * @return The provider class from the target bundle.
         * @exception Exception
         */
        public Class<?> loadClass() throws ClassNotFoundException {
            try {
                log(LogService.LOG_DEBUG, "loading class for: " + this);
                return bundle.loadClass(providerClass);
            } catch (ClassNotFoundException e) {
                log(LogService.LOG_DEBUG, "exception caught while loading " + this, e);
                throw e;
            }
        }

        /**
         * Create an instance of the registred service.
         *
         * @return The created instance.  A new instance is created on each call.
         * @exception Exception
         */
        public Object createInstance() throws Exception {
            // get the class object
            Class <?> cls = loadClass();
            try {
                // just create an instance using the default constructor
                return cls.newInstance();
            } catch (Exception e) {
                log(LogService.LOG_DEBUG, "exception caught while creating " + this, e);
                throw e;
            } catch (Error e) {
                log(LogService.LOG_DEBUG, "error caught while creating " + this, e);
                throw e;
            }
        }


        public String id() {
            return providerId;
        }

        @Override
        public String toString() {
            return "Provider interface=" + providerId + " , provider class=" + providerClass + ", bundle=" + bundle;
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
