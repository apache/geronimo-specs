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
package org.apache.geronimo.specs.activation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.activation.MailcapCommandMap;
import jakarta.activation.CommandMap;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.BundleTrackerCustomizer;

public class CommandMapBundleTrackerCustomizer implements BundleTrackerCustomizer {
    // our base Activator (used as a service source)
    private Activator activator;
    // the bundle hosting the activation code
    private Bundle activationBundle;
    // the accumulated list of provider bundles that have command definitions
    private ConcurrentMap<Long, URL> mailCaps = new ConcurrentHashMap<Long, URL>();

    public CommandMapBundleTrackerCustomizer(Activator a, Bundle b) {
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
    public Object addingBundle(Bundle bundle, BundleEvent event) {
        if (bundle.equals(activationBundle)) {
            return null;
        }

        return registerBundle(bundle);
    }

    /**
     * Perform the check for an existing mailcap file when
     * a bundle is registered.
     *
     * @param bundle The potential provider bundle.
     *
     * @return A URL object if this bundle contains a mailcap file.
     */
    private Object registerBundle(Bundle bundle) {
        URL url = bundle.getResource("/META-INF/mailcap");
        if (url != null) {
            log(LogService.LOG_DEBUG, "found mailcap at " + url);
            mailCaps.put(bundle.getBundleId(), url);
            rebuildCommandMap();
        }
        // the url marks our interest in additional activity for this
        // bundle.
        return url;
    }


    /**
     * Remove a bundle from our potential mailcap pool.
     *
     * @param bundle The potential source bundle.
     */
    protected void unregisterBundle(Bundle bundle) {
        URL mailcap = mailCaps.remove(bundle.getBundleId());
        if (mailcap != null ){
            log(LogService.LOG_DEBUG, "removing mailcap at " + mailcap);
            rebuildCommandMap();
        }
    }


    public void modifiedBundle(Bundle bundle, BundleEvent event, Object object) {
        log(LogService.LOG_DEBUG, "Bundle Considered for mailcap providers: " + bundle.getSymbolicName());
        // this will update for the new bundle
        registerBundle(bundle);
    }

    public void removedBundle(Bundle bundle, BundleEvent event, Object object) {
        unregisterBundle(bundle);
    }

    private void log(int level, String message) {
        activator.log(level, message);
    }

    private void log(int level, String message, Throwable th) {
        activator.log(level, message, th);
    }

    /**
     * Rebuild a new default command map after a change in
     * the status of bundles providing command maps.
     */
    private void rebuildCommandMap() {
        MailcapCommandMap commandMap = new MailcapCommandMap();
        for (URL url : mailCaps.values()) {
            try {
                InputStream is = url.openStream();
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = br.readLine()) != null) {
                        commandMap.addMailcap(line);
                    }
                } finally {
                    is.close();
                }
            } catch (Exception e) {
                // Ignore
            }
        }
        // this is our new default command map
        CommandMap.setDefaultCommandMap(commandMap);
    }
}
