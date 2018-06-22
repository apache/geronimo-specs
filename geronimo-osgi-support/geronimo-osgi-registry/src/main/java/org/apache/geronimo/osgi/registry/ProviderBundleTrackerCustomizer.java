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
package org.apache.geronimo.osgi.registry;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.BundleTrackerCustomizer;

public class ProviderBundleTrackerCustomizer implements BundleTrackerCustomizer {
    // our base Activator (used as a service source)
    private Activator activator;
    // the bundle hosting this registry
    private Bundle registryBundle;
    // the registry we interact with
    private ProviderRegistryImpl registry;

    public ProviderBundleTrackerCustomizer(Activator a, Bundle b, ProviderRegistryImpl r) {
        activator = a;
        registryBundle = b;
        registry = r;
    }

    /**
     * Handle the activation of a new bundle.
     *
     * @param bundle The source bundle.
     * @param event  The bundle event information.
     *
     * @return A return object.
     */
    public Object addingBundle(Bundle bundle, BundleEvent event) {
        log(LogService.LOG_DEBUG, "Bundle Considered for class providers: " + bundle.getSymbolicName());
        if (bundle.equals(registryBundle)) {
            return null;
        }

        return registry.addBundle(bundle);
    }

    public void modifiedBundle(Bundle bundle, BundleEvent event, Object object) {
        // nothing to do here
    }

    public void removedBundle(Bundle bundle, BundleEvent event, Object object) {
        // have the registry process this
        registry.removeBundle(bundle, object);
    }

    private void log(int level, String message) {
        activator.log(level, message);
    }

    private void log(int level, String message, Throwable th) {
        activator.log(level, message, th);
    }
}
