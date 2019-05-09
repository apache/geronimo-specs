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
package jakarta.xml.bind;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.geronimo.osgi.locator.ProviderLocator;

class ContextFinder {

    private static final String PLATFORM_DEFAULT_FACTORY_CLASS = "com.sun.xml.bind.v2.ContextFactory";
    private static final String JAXB_CONTEXT_PROPERTY = JAXBContext.class.getName();
    private static final String JAXB_CONTEXT_FACTORY = JAXBContext.JAXB_CONTEXT_FACTORY;

    public static JAXBContext find(String contextPath, ClassLoader classLoader, Map properties) throws JAXBException {
        contextPath = contextPath.trim();
        if (contextPath.length() == 0 || contextPath.equals(":")) {
            throw new JAXBException("Invalid contextPath");
        }
        String className = null;
        String[] packages = contextPath.split("[:]");
        for (String pkg : packages) {
            String url = pkg.replace('.', '/') + "/jaxb.properties";
            className = loadClassNameFromProperties(url, classLoader);
            if (className != null) {
                break;
            }
        }
        if (className == null) {
            className = System.getProperty(JAXB_CONTEXT_PROPERTY);
        }
        Class spi = null;
        // if no specifically specified name, check for META-INF/services, and
        // fall back to the default factory class if that fails
        if (className == null) {
            spi = loadSPIClass(JAXBContext.class, classLoader);
            if (spi == null) {
                spi = loadSpi(PLATFORM_DEFAULT_FACTORY_CLASS, classLoader);
            }
        }
        else {
            spi = loadSpi(className, classLoader);
        }
        try {
            Method m = spi.getMethod("createContext", new Class[] { String.class, ClassLoader.class, Map.class });
            return (JAXBContext) m.invoke(null, new Object[] { contextPath, classLoader, properties });
        } catch (NoSuchMethodException e) {
            // will try JAXB 1.0 compatible createContext() method
        } catch (Throwable t) {
            throw new JAXBException("Unable to create context", t);
        }

        // try old JAXB 1.0 compatible createContext() method
        try {
            Method m = spi.getMethod("createContext", new Class[] { String.class, ClassLoader.class });
            return (JAXBContext) m.invoke(null, new Object[] { contextPath, classLoader });
        } catch (Throwable t) {
            throw new JAXBException("Unable to create context", t);
        }
    }


    public static JAXBContext find(Class[] classes, Map properties) throws JAXBException {
        String className = null;
        for (Class cl : classes) {
            Package pkg = cl.getPackage();
            if (pkg != null) {
                String url = pkg.getName().replace('.', '/') + "/jaxb.properties";
                className = loadClassNameFromProperties(url, cl.getClassLoader());
                if (className != null) {
                    break;
                }
            }
        }
        if (className == null) {
            className = System.getProperty(JAXB_CONTEXT_PROPERTY);
        }
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        Class spi = null;
        // if no specifically specified name, check for META-INF/services, and
        // fall back to the default factory class if that fails
        if (className == null) {
            spi = loadSPIClass(JAXBContext.class, classLoader);
            if (spi == null) {
                spi = loadSpi(PLATFORM_DEFAULT_FACTORY_CLASS, classLoader);
            }
        }
        else {
            spi = loadSpi(className, classLoader);
        }
        try {
            Method m = spi.getMethod("createContext", new Class[] { Class[].class, Map.class });
            return (JAXBContext) m.invoke(null, new Object[] { classes, properties });
        } catch (Throwable t) {
            throw new JAXBException("Unable to create context", t);
        }
    }

    private static String loadClassNameFromProperties(String url, ClassLoader classLoader) throws JAXBException {
        try {
            InputStream is;
            if (classLoader != null) {
                is = classLoader.getResourceAsStream(url);
            } else {
                is = ClassLoader.getSystemResourceAsStream(url);
            }
            if (is != null) {
                try {
                    Properties props = new Properties();
                    props.load(is);
                    String className = props.getProperty(JAXB_CONTEXT_FACTORY);
                    if (className == null) {
                        throw new JAXBException("jaxb.properties file " + url + " should contain a " + JAXB_CONTEXT_FACTORY + " property");
                    }
                    return className.trim();
                } finally {
                    is.close();
                }
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new JAXBException(e);
        }
    }

    private static Class<?> loadSPIClass(Class<?> iface, ClassLoader classLoader) throws JAXBException {
        try {
            // delegate this to common processing
            return ProviderLocator.getServiceClass(iface.getName(), ContextFinder.class,classLoader );
        } catch (ClassNotFoundException e) {
            throw new JAXBException("Provider " + iface.getName() + " not found", e);
        }
    }

    private static Class loadSpi(String className, ClassLoader classLoader) throws JAXBException {
        try {
            return ProviderLocator.loadClass(className, ContextFinder.class, classLoader);
        } catch (ClassNotFoundException e) {
            throw new JAXBException("Provider " + className + " not found", e);
        }
    }


}
