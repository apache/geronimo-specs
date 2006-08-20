/**
 *
 * Copyright 2006 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package javax.security.auth.message.config;

import java.util.Map;

/**
 * @version $Rev$ $Date$
 */
public abstract class AuthConfigFactory {

    private static AuthConfigFactory factory;

    static AuthConfigFactory getFactory() {
        return AuthConfigFactory.factory;
    }

    static void setFactory(AuthConfigFactory factory) {
        AuthConfigFactory.factory = factory;
    }

    abstract String[] detachListener(RegistrationListener listener, String layer, String appContext);

    abstract AuthConfigProvider getConfigProvider(String layer, String appContext, RegistrationListener listener);

    abstract RegistrationContext getRegistrationContext(String registrationID);

    abstract String[] getRegistrationIDs(AuthConfigProvider provider);

    abstract void refresh();

    abstract String registerConfigProvider(String className, Map properties, String layer, String appContext, String description);

    abstract boolean removeRegistration(String registrationID);

    public static interface RegistrationContext {

        String getAppContext();

        String getDescription();

        String getMessageLayer();
    }

}
