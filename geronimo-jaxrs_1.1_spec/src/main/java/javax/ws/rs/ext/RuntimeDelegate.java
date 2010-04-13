/*
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

package javax.ws.rs.ext;

import java.lang.reflect.ReflectPermission;
import java.io.File;
import java.io.IOException;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Variant;

import org.apache.geronimo.osgi.locator.ProviderLocator;


public abstract class RuntimeDelegate {
    public static final String JAXRS_RUNTIME_DELEGATE_PROPERTY = "javax.ws.rs.ext.RuntimeDelegate";

    public static interface HeaderDelegate<T> {
        public T fromString(String str);

        public String toString(T obj);
    }

    protected RuntimeDelegate() {
        // do nothing
    }

    public abstract <T> T createEndpoint(Application app, java.lang.Class<T> type);

    public abstract UriBuilder createUriBuilder();

    public abstract Variant.VariantListBuilder createVariantListBuilder();

    public abstract <T> RuntimeDelegate.HeaderDelegate<T> createHeaderDelegate(Class<T> headerType);

    public abstract Response.ResponseBuilder createResponseBuilder();

    private static volatile RuntimeDelegate delegate;

    public static void setInstance(RuntimeDelegate delegate) throws SecurityException {
        SecurityManager secManager = System.getSecurityManager();
        if (secManager != null) {
            secManager.checkPermission(new ReflectPermission("suppressAccessChecks"));
        }
        RuntimeDelegate.delegate = delegate;
    }

    public static RuntimeDelegate getInstance() {
        if (delegate != null) {
            return delegate;
        }

        // cannot synchronize on any instance so synchronize on class
        synchronized (RuntimeDelegate.class) {
            if (delegate != null) {
                return delegate;
            }

            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

            // try META-INF/services/javax.ws.rs.ext.RuntimeDelegate
            try {
                // check the META-INF/services definitions, and return it if
                // we find something.
                Object service = ProviderLocator.getService(RuntimeDelegate.class.getName(), RuntimeDelegate.class, classLoader);
                if (service != null) {
                    delegate = (RuntimeDelegate)service;
                    return delegate;
                }
            } catch (Exception ex) {
                // ignore any errors, try additional creation methods
            } catch (Error ex) {
                // ignore any errors, try additional creation methods
            }

            String className = null;

            try {
                // try to read from $java.home/lib/jaxrpc.properties
                className =  ProviderLocator.lookupByJREPropertyFile("lib" + File.separator + "jaxrpc.properties", RuntimeDelegate.class.getName());
                if (className != null) {
                    Class<?> delegateClass = ProviderLocator.loadClass(className,
                        RuntimeDelegate.class, classLoader);
                    delegate = (RuntimeDelegate)delegateClass.newInstance();
                    return delegate;
                }
            } catch (IOException e) {
                // do nothing
            } catch (ClassNotFoundException e) {
                // do nothing
            } catch (InstantiationException e) {
                // do nothing
            } catch (IllegalAccessException e) {
                // do nothing
            }

            // try system property
            try {
                className = System.getProperty("javax.ws.rs.ext.RuntimeDelegate");
            } catch (SecurityException e) {
                // do nothing
            }

            // if the system property is null or empty go ahead and use the
            // default implementation class name

            if (className == null || "".equals(className)) {
                // dunno which should be the default. this might be interesting
                // for OSGi purposes later to somehow set the
                // "current implementation" to be the current default. dunno if
                // spec allows for that
                className = "org.apache.wink.common.internal.runtime.RuntimeDelegateImpl";
            }

            try {
                Class<?> delegateClass = ProviderLocator.loadClass(className,
                    RuntimeDelegate.class, classLoader);
                delegate = (RuntimeDelegate)delegateClass.newInstance();
                return delegate;
            } catch (ClassNotFoundException e1) {
                // do nothing
            } catch (SecurityException e) {
                // do nothing
            } catch (InstantiationException e) {
                // do nothing
            } catch (IllegalAccessException e) {
                // do nothing
            }

            throw new RuntimeException("Unable to create jax-rs RuntimeDelegate");
        }
    }
}
