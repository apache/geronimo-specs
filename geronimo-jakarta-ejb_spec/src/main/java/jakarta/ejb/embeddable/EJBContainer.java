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
//
// This source code implements specifications defined by the Java
// Community Process. In order to remain compliant with the specification
// DO NOT add / change / or delete method signatures!
//
package jakarta.ejb.embeddable;

import java.util.Collections;
import java.util.List;

import jakarta.ejb.EJBException;
import jakarta.ejb.spi.EJBContainerProvider;
import org.apache.geronimo.osgi.locator.ProviderLocator;

public abstract class EJBContainer implements AutoCloseable {

    public static final String PROVIDER = "jakarta.ejb.embeddable.provider";
    public static final String APP_NAME = "jakarta.ejb.embeddable.appName";
    public static final String MODULES = "jakarta.ejb.embeddable.modules";

    public EJBContainer() {
    }

    public abstract void close();

    public static EJBContainer createEJBContainer() {
        return createEJBContainer(Collections.EMPTY_MAP);
    }

    public static EJBContainer createEJBContainer(java.util.Map<?, ?> properties) {
        if (properties == null) {
            properties = Collections.EMPTY_MAP;
        }

        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            // go check the loader files.
            List<Object> providers = ProviderLocator.getServices(EJBContainerProvider.class.getName(), EJBContainer.class, loader);
            for (Object o : providers) {
                EJBContainer container = ((EJBContainerProvider) o).createEJBContainer(properties);
                if (container != null) {
                    return container;
                }
            }
            throw new EJBException("Provider error. No provider definition found");
        } catch (EJBException e) {
            // if the container provider throws an EJBException, don't wrap another one 
            // around the original. 
            throw e;
        } catch (Exception e) {
            throw new EJBException("Provider error. No provider found", e);
        }
    }

    public abstract javax.naming.Context getContext();
}
