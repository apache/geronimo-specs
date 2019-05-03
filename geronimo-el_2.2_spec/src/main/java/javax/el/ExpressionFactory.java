/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package javax.el;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.ServiceLoader;

/**
 *
 * @since 2.1
 */
public abstract class ExpressionFactory {

    private static final String PLATFORM_DEFAULT_FACTORY_CLASS = "org.apache.el.ExpressionFactoryImpl";

    private static final String SYSTEM_PROPERTY_NAME = "javax.el.ExpressionFactory";

    private static final String JAVA_RUNTIME_PROPERTY_FILE_LOCATION = "lib" + File.separator + "el.properties";

    static {
        try {
            ELUtils.setCachedExpressionFactory(newInstance());
        } catch (Exception e) {
        }
    }

    public abstract Object coerceToType(Object obj, Class<?> expectedType) throws ELException;

    public abstract ValueExpression createValueExpression(ELContext context, String expression, Class<?> expectedType) throws NullPointerException, ELException;

    public abstract ValueExpression createValueExpression(Object instance, Class<?> expectedType);

    public abstract MethodExpression createMethodExpression(ELContext context, String expression, Class<?> expectedReturnType, Class<?>[] expectedParamTypes) throws ELException, NullPointerException;

    public static ExpressionFactory newInstance() {
        return newInstance(null);
    }

    public static ExpressionFactory newInstance(Properties properties) {
        ExpressionFactory factory = loadExpressionFactoryImpl();
        if (properties == null) {
            return factory;
        }
        try {
            Constructor<?> constructor = factory.getClass().getConstructor(Properties.class);
            try {
                return (ExpressionFactory) constructor.newInstance(properties);
            } catch (IllegalArgumentException e) {
                throw new ELException("Fail to create ExpressionFactory instance", e);
            } catch (InstantiationException e) {
                throw new ELException("Fail to create ExpressionFactory instance", e);
            } catch (IllegalAccessException e) {
                throw new ELException("Fail to create ExpressionFactory instance", e);
            } catch (InvocationTargetException e) {
                throw new ELException("Fail to create ExpressionFactory instance", e);
            }
        } catch (SecurityException e) {
            throw new ELException("Fail to get constuctor from ExpressionFactory implementation class", e);
        } catch (NoSuchMethodException e) {
            return factory;
        }
    }

    private static ExpressionFactory newInstance0(Class<?> implClass) {
        try {
            return (ExpressionFactory) implClass.newInstance();
        } catch (IllegalAccessException e) {
            throw new ELException("Fail to create ExpressionFactory instance", e);
        } catch (InstantiationException e) {
            throw new ELException("Fail to create ExpressionFactory instance", e);
        }
    }

    private static ExpressionFactory lookupExpressionFactoryImpl(ClassLoader cl) throws ClassNotFoundException {

        String implClassName = lookupByJREPropertyFile();
        if (implClassName == null) {
            implClassName = System.getProperty(SYSTEM_PROPERTY_NAME);
            if (implClassName == null) {
                implClassName = PLATFORM_DEFAULT_FACTORY_CLASS;
            }
        }
        return newInstance0(cl.loadClass(implClassName));
    }

    private static ExpressionFactory lookupByServiceEntryURL(ClassLoader cl) throws ClassNotFoundException {
        Thread thread = Thread.currentThread();
        ClassLoader original = thread.getContextClassLoader();
        try {
            thread.setContextClassLoader(cl);
            for (ExpressionFactory factory : ServiceLoader.load(ExpressionFactory.class)) {
                return factory;
            }
        }
        finally {
            thread.setContextClassLoader(original);
        }
        return null;
    }

    private static String lookupByJREPropertyFile() {
        try {
            String jreDirectory = System.getProperty("java.home");
            File configurationFile = new File(jreDirectory + File.separator + JAVA_RUNTIME_PROPERTY_FILE_LOCATION);
            if (configurationFile.exists() && configurationFile.canRead()) {
                Properties properties = new Properties();
                InputStream in = null;
                try {
                    in = new FileInputStream(configurationFile);
                    properties.load(in);
                    return properties.getProperty(SYSTEM_PROPERTY_NAME);
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Exception e) {
                        }
                    }
                }
            }
            return null;
        } catch (IOException e) {
            throw new ELException("Fail to read configuration file", e);
        }
    }

    private static ExpressionFactory loadExpressionFactoryImpl() {

        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null) {
                cl = ClassLoader.getSystemClassLoader();
            }
            // check the META-INF/services defintions first
            ExpressionFactory factory = lookupByServiceEntryURL(cl);
            if (factory != null) {
                return factory;
            }
            // try resolving using the alternate property lookups (always returns
            // something, since there is a default
            return lookupExpressionFactoryImpl(cl);
        } catch (ClassNotFoundException e) {
            // can be thrown either as a result of a classloading failure in the service
            // lookup or a failure to directly load the class
            throw new ELException("Fail to load implementation class", e);
        }
    }
}
