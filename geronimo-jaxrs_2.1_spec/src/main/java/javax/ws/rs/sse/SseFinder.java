/*
 * #%L
 * Apache Geronimo JAX-RS Spec 2.0
 * %%
 * Copyright (C) 2003 - 2014 The Apache Software Foundation
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package javax.ws.rs.sse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Logger;

import javax.ws.rs.client.ClientBuilder;

import org.apache.geronimo.osgi.locator.ProviderLocator;

final class SseFinder {

    private static final Logger LOGGER = Logger.getLogger(SseFinder.class.getName());

    private static final String FACTORY_ID = SseEventSource.Builder.class.getName();

    private static final String SERVICE_ID = "META-INF/services/" + FACTORY_ID;

    static Object find(final String defaultClazz) throws ClassNotFoundException {
        final ClassLoader classLoader = getContextClassLoader();

        try {
            final Object delegate = ProviderLocator.getService(FACTORY_ID, ClientBuilder.class, classLoader);
            if (delegate != null) {
                return delegate;
            }

            InputStream is = null;
            if (classLoader == null) {
                is = ClassLoader.getSystemResourceAsStream(SERVICE_ID);
            } else {
                is = classLoader.getResourceAsStream(SERVICE_ID);
            }
            if (is == null) {
                final String dottedId = SERVICE_ID.replace('$', '.');
                if (classLoader == null) {
                    is = ClassLoader.getSystemResourceAsStream(dottedId);
                } else {
                    is = classLoader.getResourceAsStream(dottedId);
                }
            }

            if (is != null) {
                final BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                final String factoryClassName = rd.readLine();
                rd.close();
                if (factoryClassName != null && !"".equals(factoryClassName)) {
                    return newInstance(factoryClassName, classLoader);
                }
            }
        } catch (final Exception ex) {
            LOGGER.finest(ex.getMessage());
        }

        try {
            final File f = new File(
                    System.getProperty("java.home") + File.separator + "lib" + File.separator + "jaxrs.properties");
            if (f.exists()) {
                final Properties props = new Properties();
                props.load(new FileInputStream(f));
                final String factoryClassName = props.getProperty(FACTORY_ID);
                return newInstance(factoryClassName, classLoader);
            }
        } catch (final Exception ex) {
            LOGGER.finest(ex.getMessage());
        }

        try {
            final String systemProp = System.getProperty(FACTORY_ID);
            if (systemProp != null) {
                return newInstance(systemProp, classLoader);
            }
        } catch (final SecurityException se) {
            LOGGER.finest(se.getMessage());
        }

        if (defaultClazz == null) {
            throw new ClassNotFoundException(FACTORY_ID + " not found", null);
        }

        return newInstance(defaultClazz, classLoader);
    }

    static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    private static Object newInstance(final String className, final ClassLoader classLoader) throws ClassNotFoundException {
        try {
            Class spiClass;
            if (classLoader == null) {
                spiClass = Class.forName(className);
            } else {
                try {
                    spiClass = Class.forName(className, false, classLoader);
                } catch (final ClassNotFoundException ex) {
                    LOGGER.finest(ex.getMessage());
                    spiClass = Class.forName(className);
                }
            }
            return spiClass.getConstructor().newInstance();
        } catch (final ClassNotFoundException x) {
            throw x;
        } catch (final Exception x) {
            throw new ClassNotFoundException(x.getMessage(), x);
        }
    }

    private SseFinder() {
        // no-op
    }
}
