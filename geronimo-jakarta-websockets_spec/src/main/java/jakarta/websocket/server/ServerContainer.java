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

package jakarta.websocket.server;

import jakarta.websocket.DeploymentException;
import jakarta.websocket.WebSocketContainer;

/**
 * The ServerContainer is the specialized view of the WebSocketContainer available in server-side deployments. There is
 * one ServerContainer instance per websocket application. The ServerContainer holds the methods to be able to register
 * server endpoints during the initialization phase of the application.
 * <p/>
 * For websocket enabled web containers, developers may obtain a reference to the ServerContainer instance by retrieving
 * it as an attribute named jakarta.websocket.server.ServerContainer on the ServletContext. This way, the registration
 * methods held on this interface may be called to register server endpoints from a ServletContextListener during the
 * deployment of the WAR file containing the endpoint.
 * <p/>
 * WebSocket implementations that run outside the web container may have other means by which to provide a
 * ServerContainer instance to the developer at application deployment time.
 * <p/>
 * Once the application deployment phase is complete, and the websocket application has begun accepting incoming
 * connections, the registration methods may no longer be called.
 */
public interface ServerContainer extends WebSocketContainer {

    /**
     * Deploys the given annotated endpoint into this ServerContainer during the initialization phase of deploying the
     * application.
     * 
     * @param endpointClass
     *            the class of the annotated endpoint
     * @throws DeploymentException
     *             if the annotated endpoint was badly formed.
     * @throws IllegalStateException
     *             - if the containing websocket application has already been deployed.
     */
    void addEndpoint(Class<?> endpointClass) throws DeploymentException;

    /**
     * Deploys the given endpoint configuration into this ServerContainer during the initialization phase of deploying
     * the application.
     * 
     * @param serverConfig
     *            the configuration instance representing the logical endpoint that will be registered.
     * @throws DeploymentException
     *             if the annotated endpoint was badly formed.
     * @throws IllegalStateException
     *             - if the containing websocket application has already been deployed.
     */
    void addEndpoint(ServerEndpointConfig serverConfig) throws DeploymentException;

}
