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

package javax.websocket.server;

import java.util.Set;

import javax.websocket.Endpoint;

/**
 * Developers include implementations of ServerApplicationConfig in an archive containing websocket endpoints (WAR file, or JAR file within the WAR file) in order to specify the websocket endpoints within the archive the implementation must deploy. There is a separate method for programmatic endpoints and for annotated endpoints.
 */
public interface ServerApplicationConfig {

    /**
     * Return a set of annotated endpoint classes that the server container must deploy. The set of classes passed in to this method is the set obtained by scanning the archive containing the implementation of this interface. Therefore, this set passed in contains all the annotated endpoint classes in the JAR or WAR file containing the implementation of this interface. This set passed in may be used the build the set to return to the container for deployment.
     * 
     * @param scanned the set of all the annotated endpoint classes in the archive containing the implementation of this interface.
     * @return the non-null set of annotated endpoint classes to deploy on the server, using the empty set to indicate none.
     */
    Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned);
    
    /**
     * Return a set of ServerEndpointConfig instances that the server container will use to deploy the programmatic endpoints. The set of Endpoint classes passed in to this method is the set obtained by scanning the archive containing the implementation of this ServerApplicationConfig. This set passed in may be used the build the set of ServerEndpointConfig instances to return to the container for deployment.
     * 
     * @param endpointClasses the set of all the Endpoint classes in the archive containing the implementation of this interface.
     * @return the non-null set of ServerEndpointConfig s to deploy on the server, using the empty set to indicate none.
     */
    Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> endpointClasses);
}
