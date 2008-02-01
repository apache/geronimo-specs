/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package javax.security.auth.message.config;

import java.security.PrivilegedActionException;
import java.util.Map;

import javax.security.auth.AuthPermission;
import javax.security.auth.message.AuthException;

/**
 * @version $Rev$ $Date$
 */
public abstract class AuthConfigFactory {

    public static final String DEFAULT_FACTORY_SECURITY_PROPERTY = "authconfigprovider.factory";

    private static AuthConfigFactory factory;
    private static ClassLoader contextClassLoader;

    static {
        contextClassLoader = (ClassLoader) java.security.AccessController
                        .doPrivileged(new java.security.PrivilegedAction() {
                            public Object run() {
                                return Thread.currentThread().getContextClassLoader();
                            }
                        });
    }
    
    public static AuthConfigFactory getFactory() throws AuthException, SecurityException {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new AuthPermission("getAuthConfigFactory"));
        }
        if (factory == null) {
            String className = (String) java.security.AccessController
                            .doPrivileged(new java.security.PrivilegedAction() {
                                public Object run() {
                                    return java.security.Security.getProperty("DEFAULT_FACTORY_SECURITY_PROPERTY");
                                }
                            });
            if (className == null) {
                className = "org.apache.geronimo.jaspi.AuthConfigFactoryImpl";
            }
            try {
                final String finalClassName = className;
                factory = (AuthConfigFactory) java.security.AccessController
                                .doPrivileged(new java.security.PrivilegedExceptionAction() {
                                    public Object run() throws ClassNotFoundException, InstantiationException,
                                                    IllegalAccessException {
                                        return Class.forName(finalClassName, true, contextClassLoader).newInstance();
                                    }
                                });
            } catch (PrivilegedActionException e) {
                Exception inner = e.getException();
                if (inner instanceof InstantiationException) {
                    throw (AuthException) new AuthException("AuthConfigFactory error:"
                                    + inner.getCause().getMessage()).initCause(inner.getCause());
                } else {
                    throw (AuthException) new AuthException("AuthConfigFactory error: " + inner).initCause(inner);
                }
            }
        }
        return factory;
    }

    public static void setFactory(AuthConfigFactory factory) throws SecurityException {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new AuthPermission("setAuthConfigFactory"));
        }
        AuthConfigFactory.factory = factory;
    }


    protected AuthConfigFactory() {
    }

    public abstract String[] detachListener(RegistrationListener listener, String layer, String appContext) throws SecurityException;

    public abstract AuthConfigProvider getConfigProvider(String layer, String appContext, RegistrationListener listener);

    public abstract RegistrationContext getRegistrationContext(String registrationID);

    public abstract String[] getRegistrationIDs(AuthConfigProvider provider);

    public abstract void refresh() throws AuthException, SecurityException;

    public abstract String registerConfigProvider(AuthConfigProvider provider, String layer, String appContext, String description) throws AuthException, SecurityException;

    public abstract String registerConfigProvider(String className, Map properties, String layer, String appContext, String description) throws AuthException, SecurityException;

    public abstract boolean removeRegistration(String registrationID) throws SecurityException;

    public static interface RegistrationContext {

        String getAppContext();

        String getDescription();

        String getMessageLayer();

        boolean isPersistent();
        
    }

}
