/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package javax.enterprise.inject.spi;

import javax.enterprise.inject.Instance;
import java.util.ServiceLoader;

/**
 * <p>Static helper class to access the {@link BeanManager}</p>
 *
 * TODO not yet implemented!
 *
 * <p>Usage:
 * <pre>
 * BeanManager bm = CDI.current().getBeanManager();
 * </pre>
 * </p>
 *
 *
 * @since 1.1
 */
public abstract class CDI<T> implements Instance<T>
{
    private static volatile CDI INSTANCE; // temporary implementation

    public static CDI<Object> current()
    {
        if (INSTANCE == null)
        {
            INSTANCE = ServiceLoader.load(CDIProvider.class).iterator().next().getCDI();
        }
        return INSTANCE; //X TODO implement!
    }

    /**
     * <p>A container or an application can set this manually. If not
     * we will use the {@link java.util.ServiceLoader} and use the
     * first service we find.</p>
     *
     * TODO: clarify if this is per 'application' or general?
     *
     * @param provider to use
     */
    public static void setCDIProvider(CDIProvider provider)
    {
        //X TODO implement!
        if (provider == null)
        {
            INSTANCE = null;
        }
        else
        {
            INSTANCE = provider.getCDI();
        }
    }

    public abstract BeanManager getBeanManager();
}
