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
//
// This source code implements specifications defined by the Java
// Community Process. In order to remain compliant with the specification
// DO NOT add / change / or delete method signatures!
//
package javax.ejb.embeddable;

import javax.ejb.EJBException;
import javax.ejb.spi.EJBContainerProvider;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;

import org.apache.geronimo.osgi.locator.ProviderLocator;

public abstract class EJBContainer {

    public static final String PROVIDER = "javax.ejb.embeddable.provider";
    public static final String APP_NAME = "javax.ejb.embeddable.appName";
    public static final String MODULES = "javax.ejb.embeddable.modules";

    public EJBContainer() {
    }

    public abstract void close();

    public static EJBContainer createEJBContainer() {
        return createEJBContainer(Collections.EMPTY_MAP);
    }

    public static EJBContainer createEJBContainer(java.util.Map<?, ?> properties) {
        if (properties == null) {
            properties = Collections.EMPTY_MAP;
        }

        Object o = properties.get(PROVIDER);

        if (o instanceof String) {
            String providerName = (String) o;

            Class providerClass;

            try {
                providerClass = ProviderLocator.loadClass(providerName);
            } catch (Exception e) {
                throw new EJBException("Invalid or inaccessible provider class: " + providerName, e);
            }

            try {
                EJBContainerProvider provider = (EJBContainerProvider) providerClass.newInstance();
                return provider.createEJBContainer(properties);
            } catch (Exception e) {
                throw new EJBException("Provider error. Provider: " + providerName, e);
            }
        } else {
            // do the services search processing.
            try {
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                // go check the loader files.
                EJBContainerProvider provider = (EJBContainerProvider)ProviderLocator.getService(EJBContainerProvider.class.getName(), EJBContainer.class, loader);
                if (provider != null) {
                    return provider.createEJBContainer(properties);
                }
                else {
                    throw new EJBException("Provider error. No provider definition found");
                }
            } catch (Exception e) {
                throw new EJBException("Provider error. No provider found", e);
            }
        }
    }

    public abstract javax.naming.Context getContext();
}
