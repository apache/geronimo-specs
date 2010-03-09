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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.apache.geronimo.osgi.locator.ProviderLocator;

/**
 *
 * @since 2.1
 */
public abstract class ExpressionFactory {

    private static final String PLATFORM_DEFAULT_FACTORY_CLASS = "org.apache.el.ExpressionFactoryImpl";

    private static final String EXPRESSION_FACTORY_SERVICE_ENTRY_URL = "META-INF/services/javax.el.ExpressionFactory";

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
        Class<?> implClass = loadExpressionFactoryImplClass();
        if (properties == null) {
            return newInstance0(implClass);
        }
        try {
            Constructor<?> constructor = implClass.getConstructor(Properties.class);
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
            return newInstance0(implClass);
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

    private static String lookupExpressionFactoryImplClass() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = ClassLoader.getSystemClassLoader();
        }
        String implClassName = null;
        implClassName = lookupByServiceEntryURL(cl);
        if (implClassName == null) {
            implClassName = lookupByJREPropertyFile();
            if (implClassName == null) {
                implClassName = System.getProperty(SYSTEM_PROPERTY_NAME);
                if (implClassName == null) {
                    implClassName = PLATFORM_DEFAULT_FACTORY_CLASS;
                }
            }
        }
        return implClassName;
    }

    private static String lookupByServiceEntryURL(ClassLoader cl) {
        InputStream in = null;
        try {
            in = cl.getResourceAsStream(EXPRESSION_FACTORY_SERVICE_ENTRY_URL);
            if (in != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                return bufferedReader.readLine().trim();
            } else {
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            throw new ELException("Fail to read " + EXPRESSION_FACTORY_SERVICE_ENTRY_URL, e);
        } catch (IOException e) {
            throw new ELException("Fail to read " + EXPRESSION_FACTORY_SERVICE_ENTRY_URL, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
    }

    private static String lookupByJREPropertyFile() {
        String jreDirectory = System.getProperty("java.home");
        File configurationFile = new File(jreDirectory + File.separator + JAVA_RUNTIME_PROPERTY_FILE_LOCATION);
        if (configurationFile.exists() && configurationFile.canRead()) {
            Properties properties = new Properties();
            InputStream in = null;
            try {
                in = new FileInputStream(configurationFile);
                properties.load(in);
                return properties.getProperty(SYSTEM_PROPERTY_NAME);
            } catch (IOException e) {
                throw new ELException("Fail to read " + configurationFile.getAbsolutePath(), e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e) {
                    }
                }
            }
        } else {
            return null;
        }
    }

    private static Class<?> loadExpressionFactoryImplClass() {
        String implClassName = null;
        try {
            implClassName = lookupExpressionFactoryImplClass();
            return ProviderLocator.loadClass(implClassName);
        } catch (ClassNotFoundException e) {
            throw new ELException("Fail to load implementation class " + implClassName, e);
        }
    }
}
