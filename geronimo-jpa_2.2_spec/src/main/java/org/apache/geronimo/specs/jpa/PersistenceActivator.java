/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.specs.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceProviderResolver;
import javax.persistence.spi.PersistenceProviderResolverHolder;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.apache.geronimo.osgi.locator.Activator;

/**
 * Used to discover/resolve JPA providers in an OSGi environment.
 *
 * @version $Rev$ $Date$
 */
public class PersistenceActivator extends Activator implements BundleActivator, PersistenceProviderResolver {

    public static final String PERSISTENCE_PROVIDER = PersistenceProvider.class.getName();

    private Map<String, PersistenceProvider> providers = new WeakHashMap<String, PersistenceProvider>();
    private BundleContext ctx = null;
    private ServiceTracker tracker = null;

    /* (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext arg0) throws Exception {
        super.start(arg0);
        // bundle context for later ServiceReference lookups
        ctx = arg0;

        // track providers as they register themselves
        ServiceTrackerCustomizer customizer = new PersistenceTracker(this);
        tracker = new ServiceTracker(ctx, PERSISTENCE_PROVIDER, customizer);
        tracker.open();

        // configure JPA provider resolver for OSGi
        PersistenceProviderResolverHolder.setPersistenceProviderResolver(this);
    }

    /* (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext arg0) throws Exception {
        // cleanup provider tracker
        tracker.close();
        tracker = null;

        // cleanup providers and remove ourselves as a JPA provider resolver
        PersistenceProviderResolverHolder.setPersistenceProviderResolver(null);
        providers.clear();

        // cleanup context
        ctx = null;

        super.stop(arg0);
    }


    /* (non-Javadoc)
     * @see javax.persistence.spi.PersistenceProviderResolver#clearCachedProviders()
     */
    public void clearCachedProviders() {
        // no-op - handled by stop() in OSGi environment
    }

    /* (non-Javadoc)
     * @see javax.persistence.spi.PersistenceProviderResolver#getPersistenceProviders()
     */
    public List<PersistenceProvider> getPersistenceProviders() {
        return new ArrayList<PersistenceProvider>(providers.values());
    }

    protected PersistenceProvider addProvider(ServiceReference ref) {
        PersistenceProvider provider = (PersistenceProvider) ctx.getService(ref);
        String name = (String) ref.getProperty(PERSISTENCE_PROVIDER);
        providers.put(name, provider);
        return provider;
    }

    protected void removeProvider(ServiceReference ref) {
        String name = (String) ref.getProperty(PERSISTENCE_PROVIDER);
        providers.remove(name);
    }

}
