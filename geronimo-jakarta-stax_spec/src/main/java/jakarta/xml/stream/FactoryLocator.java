/*
 **
 ** Licensed to the Apache Software Foundation (ASF) under one
 ** or more contributor license agreements.  See the NOTICE file
 ** distributed with this work for additional information
 ** regarding copyright ownership.  The ASF licenses this file
 ** to you under the Apache License, Version 2.0 (the
 ** "License"); you may not use this file except in compliance
 ** with the License.  You may obtain a copy of the License at
 **
 **  http://www.apache.org/licenses/LICENSE-2.0
 **
 ** Unless required by applicable law or agreed to in writing,
 ** software distributed under the License is distributed on an
 ** "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 ** KIND, either express or implied.  See the License for the
 ** specific language governing permissions and limitations
 ** under the License.
 */
package jakarta.xml.stream;

import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;

import java.util.Properties;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.geronimo.osgi.locator.ProviderLocator;

/*
 * Here is the beef on the finding the Factory Class
 *
 * 1. Use the jakarta.xml.stream.XMLInputFactory system property. 2. Use the
 * properties file "lib/stax.properties" in the JRE directory. This
 * configuration file is in standard java.util.Properties format and contains
 * the fully qualified name of the implementation class with the key being the
 * system property defined above. 3. Use the Services API (as detailed in the
 * JAR specification), if available, to determine the classname. The Services
 * API will look for a classname in the file
 * META-INF/services/jakarta.xml.stream.XMLInputFactory in jars available to the
 * runtime. Platform default XMLInputFactory instance.
 *
 * If the user provided a classloader we'll use that...if not, we'll assume the
 * classloader of this class.
 */

class FactoryLocator {
    static Object locate(String factoryId) throws FactoryConfigurationError {
        return locate(factoryId, null);
    }

    static Object locate(String factoryId, String altClassName)
            throws FactoryConfigurationError {
        return locate(factoryId, altClassName,
                              Thread.currentThread().getContextClassLoader());
    }

    static Object locate(String factoryId, String altClassName,
            ClassLoader classLoader) throws FactoryConfigurationError {
        // NOTE:  The stax spec uses the following lookup order, which is the reverse from what is specified
        // most of the APIs:
        // 1. Use the jakarta.xml.stream.XMLInputFactory system property.
        // 2. Use the properties file lib/xml.stream.properties in the JRE directory. This configuration
        // file is in standard java.util.Properties format and contains the fully qualified name of the
        // implementation class with the key being the system property defined in step 1.
        // 3. Use the Services API (as detailed in the JAR specification), if available, to determine the
        // classname. The Services API looks for a classname in the file META-INF/services/
        // jakarta.xml.stream.XMLInputFactory in jars available to the runtime.
        // 4. Platform default XMLInputFactory instance.


        // Use the system property first
        try {
            String systemProp = System.getProperty(factoryId);
            if (systemProp != null) {
                return newInstance(systemProp, classLoader);
            }
        } catch (SecurityException se) {
        }

        try {
            // NOTE:  The StAX spec gives this property file name as xml.stream.properties, but the javadoc and the maintenance release
            // state this is stax.properties.
            String factoryClassName =  ProviderLocator.lookupByJREPropertyFile("lib" + File.separator + "stax.properties", factoryId);
            if (factoryClassName != null) {
                return newInstance(factoryClassName, classLoader);
            }
        } catch (Exception ex) {
        }

        try {
            // check the META-INF/services definitions, and return it if
            // we find something.
            Object service = ProviderLocator.getService(factoryId, FactoryLocator.class, classLoader);
            if (service != null) {
                return service;
            }
        } catch (Exception ex) {
        }

        if (altClassName == null) {
            throw new FactoryConfigurationError("Unable to locate factory for "
                    + factoryId + ".", null);
        }
        return newInstance(altClassName, classLoader);
    }

    private static Object newInstance(String className, ClassLoader classLoader)
            throws FactoryConfigurationError {
        try {
            return ProviderLocator.loadClass(className, FactoryLocator.class, classLoader).newInstance();
        } catch (ClassNotFoundException x) {
              throw new FactoryConfigurationError("Requested factory "
                      + className + " cannot be located.  Classloader ="
                      + classLoader.toString(), x);
        } catch (Exception x) {
            throw new FactoryConfigurationError("Requested factory "
                    + className + " could not be instantiated: " + x, x);
        }
    }
}
