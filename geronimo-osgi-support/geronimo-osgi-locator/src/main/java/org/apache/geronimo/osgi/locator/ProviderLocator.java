/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.geronimo.osgi.locator;

import java.util.ArrayList;
import java.util.List;

import org.apache.geronimo.osgi.registry.api.ProviderRegistry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class ProviderLocator {
    // our bundle context
    static private BundleContext context;
    // a service tracker for the registry service
    // NB:  This is declared as just Object to avoid classloading issues if we're running
    // outside of an OSGi environment.
    static private Object registryTracker;

    private ProviderLocator() {
        // private constructor to prevent an instance from getting created.
    }

    /**
     * initialize the tracker statics for this bundle
     *
     * @param c      The starup BundleContext.
     */
    public static void init(BundleContext c) {
        context = c;
        // just create a tracker for our lookup service
        registryTracker = new ServiceTracker(context, ProviderRegistry.class.getName(), null);
        ((ServiceTracker)registryTracker).open();
    }


    /**
     * Cleanup resources on bundle shutdown.
     */
    public static void destroy() {
        // shutdown our tracking of the provider registry.
        ((ServiceTracker)registryTracker).close();
        registryTracker = null;
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
    static public Class<?> locate(String providerId) {
        // if not initialized in an OSGi environment, this is a failure
        if (registryTracker == null) {
            return null;
        }
        // get the service, if it exists.  NB:  if the tracker exists, then we
        // were able to load the interface class in the first place, so we don't
        // need to protect against that.
        ProviderRegistry registry = (ProviderRegistry)((ServiceTracker)registryTracker).getService();
        // it is also a failure if the service is not there.
        if (registry == null) {
            return null;
        }
        // the rest of the work is done by the registry
        return registry.locate(providerId);
    }

    /**
     * Locate all class files that match a given factory id.
     *
     * @param providerId The target provider identifier.
     *
     * @return A List containing the class objects corresponding to the
     *         provider identifier.  Returns an empty list if no
     *         matching classes can be located.
     */
    static public List<Class<?>> locateAll(String providerId) {
        // if not initialized in an OSGi environment, this is a lookup failure
        if (registryTracker == null) {
            return new ArrayList<Class<?>>();
        }
        // get the service, if it exists.  NB:  if the tracker exists, then we
        // were able to load the interface class in the first place, so we don't
        // need to protect against that.
        ProviderRegistry registry = (ProviderRegistry)((ServiceTracker)registryTracker).getService();
        // it is also a failure if the service is not there.
        if (registry == null) {
            return new ArrayList<Class<?>>();
        }
        // the rest of the work is done by the registry
        return registry.locateAll(providerId);
    }
}
