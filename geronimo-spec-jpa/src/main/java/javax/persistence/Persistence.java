/**
 *
 * Copyright 2006 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

//
// This source code implements specifications defined by the Java
// Community Process. In order to remain compliant with the specification
// DO NOT add / change / or delete method signatures!
//
package javax.persistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.persistence.spi.PersistenceProvider;

/**
 * @version $Revision$ $Date$
 */

/**
 * Bootstrap class that is used to obtain {@link javax.persistence.EntityManagerFactory}
 * references.
 */
public class Persistence {

    static final String PERSISTENCE_PROVIDER_PROPERTY = "javax.persistence.provider";
    static final String PERSISTENCE_PROVIDER_SERVICE = "META-INF/services/"
        + PersistenceProvider.class.getName();

    /**
     * Create and return an EntityManagerFactory for the named persistence unit.
     *
     * @param persistenceUnitName The name of the persistence unit
     * @return The factory that creates EntityManagers configured according to the
     *         specified persistence unit
     */
    public static EntityManagerFactory createEntityManagerFactory(
            String persistenceUnitName) {
        return createEntityManagerFactory(persistenceUnitName, Collections.EMPTY_MAP);
            }

    /**
     * Create and return an EntityManagerFactory for the named persistence unit using the
     * given properties.
     *
     * @param persistenceUnitName The name of the persistence unit
     * @param properties          Additional properties to use when creating the factory. The values of
     *                            these properties override any values that may have been configured
     *                            elsewhere.
     * @return The factory that creates EntityManagers configured according to the
     *         specified persistence unit.
     */
    public static EntityManagerFactory createEntityManagerFactory(
            String persistenceUnitName,
            Map properties) {

        if (properties == null) {
            properties = Collections.EMPTY_MAP;
        }

        // start by loading a provider explicitly specified in properties. The spec
        // doesn't seem to forbid providers that are not deployed as a service
        Object providerName = properties.get(PERSISTENCE_PROVIDER_PROPERTY);
        if (providerName instanceof String) {
            EntityManagerFactory factory = createFactory(
                    providerName.toString(),
                    persistenceUnitName,
                    properties);
            if (factory != null) {
                return factory;
            }
        }

        // load correctly deployed providers
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> providers = loader
                .getResources(PERSISTENCE_PROVIDER_SERVICE);
            while (providers.hasMoreElements()) {

                String name = getProviderName(providers.nextElement());

                if (name != null) {

                    EntityManagerFactory factory = createFactory(
                            name,
                            persistenceUnitName,
                            properties);

                    if (factory != null) {
                        return factory;
                    }
                }
            }
        }
        catch (IOException e) {
            // spec doesn't mention any exceptions thrown by this method
        }

        return null;
            }

    static String getProviderName(URL url) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(
                    url.openStream(),
                    "UTF-8"));

        String providerName;

        try {
            providerName = in.readLine();
        }
        finally {
            in.close();
        }

        if (providerName != null) {
            providerName = providerName.trim();
        }

        return providerName;
    }

    static EntityManagerFactory createFactory(
            String providerName,
            String persistenceUnitName,
            Map properties)
        throws PersistenceException {

        Class providerClass;
        try {
            providerClass = Class.forName(providerName, true, Thread
                    .currentThread().getContextClassLoader());
        } 
        catch (Exception e) {
            throw new PersistenceException(
                    "Invalid or inaccessible provider class: " + providerName,
                    e);
        }

        try {
            PersistenceProvider provider = (PersistenceProvider) providerClass
                .newInstance();
            return provider.createEntityManagerFactory(persistenceUnitName,
                    properties);
        }
        catch (Exception e) {
            throw new PersistenceException("Provider error. Provider: "
                    + providerName, e);
        }
    }
}
