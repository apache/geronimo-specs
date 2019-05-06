/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package jakarta.websocket;

import java.util.ServiceLoader;

/**
 * Provider class that allows the developer to get a reference to the implementation of the WebSocketContainer. The
 * provider class uses the ServiceLoader to load an implementation of ContainerProvider. Specifically, the fully
 * qualified classname of the container implementation of ContainerProvider must be listed in the
 * META-INF/services/jakarta.websocket.ContainerProvider file in the implementation JAR file.
 */
public abstract class ContainerProvider {

    private static ServiceLoader<WebSocketContainer> loader = ServiceLoader.load(WebSocketContainer.class);

    /**
     * Obtain a new instance of a WebSocketContainer. The method looks for the ContainerProvider implementation class in
     * the order listed in the META-INF/services/jakarta.websocket.ContainerProvider file, returning the
     * WebSocketContainer implementation from the ContainerProvider implementation that is not null.
     * 
     * @return an implementation provided instance of type WebSocketContainer
     */
    public static WebSocketContainer getWebSocketContainer() {
        for (WebSocketContainer cntr : loader) {
            return cntr;
        }
        return null;
    }

    public ContainerProvider() {
    }

    /**
     * Load the container implementation.
     * 
     * @return the implementation class
     */
    protected abstract WebSocketContainer getContainer();

}
