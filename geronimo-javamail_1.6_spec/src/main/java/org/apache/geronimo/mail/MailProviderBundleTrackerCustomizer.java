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
package org.apache.geronimo.mail;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTrackerCustomizer;

public class MailProviderBundleTrackerCustomizer implements BundleTrackerCustomizer {
    // our base Activator (used as a service source)
    private final Activator activator;
    // the bundle hosting the activation code
    private final Bundle activationBundle;

    public MailProviderBundleTrackerCustomizer(final Activator a, final Bundle b) {
        activator = a;
        activationBundle = b;
    }

    /**
     * Handle the activation of a new bundle.
     *
     * @param bundle The source bundle.
     * @param event  The bundle event information.
     *
     * @return A return object.
     */
    public Object addingBundle(final Bundle bundle, final BundleEvent event) {
        if (bundle.equals(activationBundle)) {
            return null;
        }

        return MailProviderRegistry.registerBundle(bundle);
    }


    public void modifiedBundle(final Bundle bundle, final BundleEvent event, final Object object) {
        // this will update for the new bundle
        MailProviderRegistry.registerBundle(bundle);
    }

    public void removedBundle(final Bundle bundle, final BundleEvent event, final Object object) {
        MailProviderRegistry.unregisterBundle(bundle);
    }

    private void log(final int level, final String message) {
        activator.log(level, message);
    }

    private void log(final int level, final String message, final Throwable th) {
        activator.log(level, message, th);
    }
}
