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

import java.net.URL;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.osgi.framework.Bundle;

/**
 * The activator that starts and manages the tracking of
 * JAF activation command maps
 */
public class MailProviderRegistry {
    // a list of all active mail provider config files
    static ConcurrentMap<Long, URL> providers = new ConcurrentHashMap<Long, URL>();
    // a list of all active default provider config files
    static ConcurrentMap<Long, URL> defaultProviders = new ConcurrentHashMap<Long, URL>();

    /**
     * Perform the check for an existing mailcap file when
     * a bundle is registered.
     *
     * @param bundle The potential provider bundle.
     *
     * @return A URL object if this bundle contains a mailcap file.
     */
    static Object registerBundle(final Bundle bundle) {
        // potential tracker return result
        Object result = null;
        // a given bundle might have a javamail.providers definition and/or a
        // default providers definition.
        URL url = bundle.getResource("META-INF/javamail.providers");
        if (url != null) {
            providers.put(bundle.getBundleId(), url);
            // this indicates our interest
            result = url;
        }

        url = bundle.getResource("META-INF/javamail.default.providers");
        if (url != null) {
            defaultProviders.put(bundle.getBundleId(), url);
            // this indicates our interest
            result = url;
        }
        // the url marks our interest in additional activity for this
        // bundle.
        return result;
    }


    /**
     * Remove a bundle from our potential mailcap pool.
     *
     * @param bundle The potential source bundle.
     */
    static void unregisterBundle(final Bundle bundle) {
        // remove these items
        providers.remove(bundle.getBundleId());
        defaultProviders.remove(bundle.getBundleId());
    }

    /**
     * Retrieve any located provider definitions
     * from bundles.
     *
     * @return A collection of the provider definition file
     *         URLs.
     */
    public static Collection<URL> getProviders() {
        return providers.values();
    }

    /**
     * Retrieve any located default provider definitions
     * from bundles.
     *
     * @return A collection of the default provider definition file
     *         URLs.
     */
    public static Collection<URL> getDefaultProviders() {
        return defaultProviders.values();
    }
}
