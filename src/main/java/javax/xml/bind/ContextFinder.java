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
package javax.xml.bind;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.io.InputStreamReader;

class ContextFinder {

    private static final String PLATFORM_DEFAULT_FACTORY_CLASS = "com.sun.xml.bind.v2.ContextFactory";
    private static final String JAXB_CONTEXT_PROPERTY = JAXBContext.class.getName();
    private static final String JAXB_CONTEXT_FACTORY = JAXBContext.JAXB_CONTEXT_FACTORY;

    ContextFinder() {
    }

    public static JAXBContext find(String contextPath, ClassLoader classLoader, Map properties) throws JAXBException {
        String factoryClassName = null;

        // Searching jaxb.properties
        if (factoryClassName == null) {
            StringTokenizer packages = new StringTokenizer(contextPath, ":");
            if (!packages.hasMoreTokens()) {
                throw new JAXBException("No package name is given");
            }
            while (packages.hasMoreTokens()) {
                String packageName = packages.nextToken(":").replace('.', '/');
                String propFileName = packageName + "/jaxb.properties";
                Properties props = loadJAXBProperties(classLoader, propFileName);
                if (props != null) {
                    if (props.containsKey(JAXB_CONTEXT_FACTORY)) {
                        factoryClassName = props.getProperty(JAXB_CONTEXT_FACTORY);
                        break;
                    } else {
                        throw new JAXBException("jaxb.properties in package " + packageName + " does not contain the "
                                                    + JAXB_CONTEXT_FACTORY + " property");
                    }
                }
            }
        }

        // Searching the system property
        if (factoryClassName == null) {
            factoryClassName = System.getProperty(JAXB_CONTEXT_PROPERTY, null);
        }

        // Searching META-INF/services
        if (factoryClassName == null) {
            try {
                String resource = "META-INF/services/" + JAXB_CONTEXT_PROPERTY;
                InputStream resourceStream = classLoader.getResourceAsStream(resource);
                if (resourceStream != null) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(resourceStream, "UTF-8"));
                    factoryClassName = r.readLine().trim();
                }
            } catch (IOException e) {
                throw new JAXBException(e);
            }
        }

        // Trying to create the platform default provider
        if (factoryClassName == null) {
            factoryClassName = PLATFORM_DEFAULT_FACTORY_CLASS;
        }

        // Create the jaxb context
        return newInstance(contextPath, factoryClassName, classLoader, properties);
    }


    public static JAXBContext find(Class classes[], Map properties) throws JAXBException {
        String factoryClassName = null;

        // Checking the packages
        if (factoryClassName ==  null) {
            for (Class c : classes) {
                ClassLoader classLoader = c.getClassLoader();
                Package pkg = c.getPackage();
                if (pkg == null) {
                    continue;
                }
                String packageName = pkg.getName().replace('.', '/');
                String resourceName = packageName + "/jaxb.properties";
                Properties props = loadJAXBProperties(classLoader, resourceName);
                if (props == null) {
                    continue;
                }
                if (props.containsKey(JAXBContext.JAXB_CONTEXT_FACTORY)) {
                    factoryClassName = props.getProperty(JAXBContext.JAXB_CONTEXT_FACTORY).trim();
                    break;
                } else {
                    throw new JAXBException("jaxb.properties in package " + packageName + " does not contain the " + JAXBContext.JAXB_CONTEXT_FACTORY + " property");
                }
            }
        }

        // Checking system property JAXB_CONTEXT_PROPERTY
        if (factoryClassName == null) {
            factoryClassName = System.getProperty(JAXB_CONTEXT_PROPERTY, null);
        }

        // Checking META-INF/services
        if (factoryClassName == null) {
            try {
                String resource = "META-INF/services/" + JAXB_CONTEXT_PROPERTY;
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                URL resourceURL;
                if (classLoader == null) {
                    resourceURL = ClassLoader.getSystemResource(resource);
                } else {
                    resourceURL = classLoader.getResource(resource);
                }
                if (resourceURL != null) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(resourceURL.openStream(), "UTF-8"));
                    factoryClassName = r.readLine().trim();
                }
            } catch (IOException e) {
                throw new JAXBException(e);
            }
        }

        // Trying to create the platform default provider
        if (factoryClassName == null) {
            factoryClassName = PLATFORM_DEFAULT_FACTORY_CLASS;
        }

        // Create the jaxb context
        return newInstance(classes, properties, factoryClassName);
    }

    private static JAXBContext newInstance(String contextPath, String className, ClassLoader classLoader, Map properties) throws JAXBException {
        Class spiClass = loadSpiClass(className, classLoader);
        try {
            Object context = null;
            try {
                Method m = spiClass.getMethod("createContext", new Class[] { String.class, ClassLoader.class, Map.class });
                context = m.invoke(null, new Object[] { contextPath, classLoader, properties });
            } catch(NoSuchMethodException e) { 
            }
            if (context == null) {
                Method m = spiClass.getMethod("createContext", new Class[] { String.class, ClassLoader.class });
                context = m.invoke(null, new Object[] { contextPath, classLoader });
            }
            if (!(context instanceof JAXBContext)) {
                handleClassCastException(context.getClass(), JAXBContext.class);
            }
            return (JAXBContext)context;
        } catch (InvocationTargetException e) {
            handleInvocationTargetException(e);
            Throwable x = e;
            if (e.getTargetException() != null) {
                x = e.getTargetException();
            }
            throw new JAXBException("Provider " + className + " could not be instanciated: " + x, x);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new JAXBException("Provider " + className + " could not be instanciated: " + e, e);
        }
    }

    private static Class loadSpiClass(String className, ClassLoader cl) throws JAXBException {
        Class spiClass;
        try {
            // If inside an OSGi context, leverage it
            spiClass = org.apache.servicemix.specs.locator.OsgiLocator.locate(JAXBContext.class.getName());
            if (spiClass != null) {
                return spiClass;
            }
        } catch (Throwable t) {
        }
        try {
            if (cl != null) {
                spiClass = cl.loadClass(className);
            } else {
                spiClass = Class.forName(className);
            }
        } catch (ClassNotFoundException e) {
            throw new JAXBException("Provider " + className + " not found", e);
        }
        return spiClass;
    }

    private static JAXBContext newInstance(Class classes[], Map properties, String className) throws JAXBException {
        Class spiClass = loadSpiClass(className, Thread.currentThread().getContextClassLoader());
        return newInstance(classes, properties, spiClass);
    }

    private static JAXBContext newInstance(Class classes[], Map properties, Class spiClass) throws JAXBException {
        Method m;
        try {
            m = spiClass.getMethod("createContext", new Class[] { Class[].class, Map.class });
        } catch (NoSuchMethodException e) {
            throw new JAXBException(e);
        }
        try {
            Object context = m.invoke(null, new Object[] { classes, properties });
            if (!(context instanceof JAXBContext)) {
                handleClassCastException(context.getClass(), JAXBContext.class);
            }
            return (JAXBContext)context;
        } catch (IllegalAccessException e) {
            throw new JAXBException(e);
        } catch (InvocationTargetException e) {
            handleInvocationTargetException(e);
            Throwable x = e;
            if (e.getTargetException() != null) {
                x = e.getTargetException();
            }
            throw new JAXBException(x);
        }
    }

    private static Properties loadJAXBProperties(ClassLoader classLoader, String propFileName) throws JAXBException {
        Properties props = null;
        try {
            URL url;
            if (classLoader == null) {
                url = ClassLoader.getSystemResource(propFileName);
            } else {
                url = classLoader.getResource(propFileName);
            }
            if (url != null) {
                props = new Properties();
                InputStream is = url.openStream();
                props.load(is);
                is.close();
            }
        } catch (IOException ioe) {
            throw new JAXBException(ioe.toString(), ioe);
        }
        return props;
    }

    private static URL which(Class clazz, ClassLoader loader) {
        String classnameAsResource = clazz.getName().replace('.', '/') + ".class";
        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }
        return loader.getResource(classnameAsResource);
    }

    private static URL which(Class clazz) {
        return which(clazz, clazz.getClassLoader());
    }

    private static void handleInvocationTargetException(InvocationTargetException x) throws JAXBException {
        Throwable t = x.getTargetException();
        if (t != null) {
            if (t instanceof JAXBException) {
                throw (JAXBException) t;
            }
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            }
            if (t instanceof Error) {
                throw (Error) t;
            }
        }
    }

    private static void handleClassCastException(Class originalType, Class targetType) throws JAXBException {
        URL targetTypeURL = which(targetType);
        throw new JAXBException("ClassCastException: attempting to cast " +
                                originalType.getClass().getClassLoader().getResource("javax/xml/bind/JAXBContext.class").toString() +
                                " to " + targetTypeURL.toString() + ".  Please make sure that you are specifying the proper ClassLoader.");
    }

}
