/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package jakarta.xml.soap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.geronimo.osgi.locator.ProviderLocator;

/**
 * This class is used to locate factory classes for jakarta.xml.soap. It has package scope since it is
 * not part of JAXM and should not be accessed from other packages.
 */
class FactoryFinder {
    /**
     * instantiates an object go the given classname.
     *
     * @param factoryClassName
     * @return a factory object
     * @throws SOAPException
     */
    private static Object newInstance(String factoryClassName, ClassLoader classLoader) throws SOAPException {
        ClassLoader classloader = null;
        try {
            classloader = Thread.currentThread().getContextClassLoader();
        } catch (Exception exception) {
            throw new SOAPException(exception.toString(), exception);
        }

        try {
            return ProviderLocator.loadClass(factoryClassName, FactoryFinder.class, classloader).newInstance();
        } catch (ClassNotFoundException classnotfoundexception) {
            throw new SOAPException(
                    "Provider " + factoryClassName + " not found",
                    classnotfoundexception);
        } catch (Exception exception) {
            throw new SOAPException(
                    "Provider " + factoryClassName +
                            " could not be instantiated: " +
                            exception,
                    exception);
        }
    }


    /**
     * Instantiates a factory object given the factory's property name.
     *
     * @param factoryPropertyName
     * @return a factory object
     * @throws SOAPException
     */
    static Object find(Class<?> factoryType, String defaultFactoryClassName) throws SOAPException {
        String factoryPropertyName = factoryType.getName();
        ClassLoader classLoader = null;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        } catch (Exception exception) {
            throw new SOAPException(exception.toString(), exception);
        }

        try {
            // check the META-INF/services definitions, and return it if
            // we find something.
            Object service = ProviderLocator.getService(factoryPropertyName, FactoryFinder.class, classLoader);
            if (service != null) {
                return service;
            }
        } catch (Exception ex) {
        }

        try {
            String factoryClassName =  ProviderLocator.lookupByJREPropertyFile("lib" + File.separator + "jaxm.properties", factoryPropertyName);
            if (factoryClassName != null) {
                return newInstance(factoryClassName, classLoader);
            }
        } catch (Exception ex) {
        }

        // Use the system property last
        try {
            String systemProp = System.getProperty(factoryPropertyName);
            if (systemProp != null) {
                return newInstance(systemProp, classLoader);
            }
        } catch (SecurityException se) {
        }
        return newInstance(defaultFactoryClassName, classLoader);
    }
}
