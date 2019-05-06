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

import java.util.List;
import java.util.Map;

/**
 * The endpoint configuration contains all the information needed during the handshake process for this end point. All
 * endpoints specify, for example, a URI. In the case of a server endpoint, the URI signifies the URI to which the
 * endpoint will be mapped. In the case of a client application the URI signifies the URI of the server to which the
 * client endpoint will attempt to connect.
 */
public interface EndpointConfig {

    /**
     * Return the Encoder implementation classes configured. These will be instantiated by the container to encode
     * custom objects passed into the send() methods on remote endpoints.
     * 
     * @return the encoder implementation classes, an empty list if none.
     */
    List<Class<? extends Encoder>> getEncoders();

    /**
     * Return the Decoder implementation classes configured. These will be instantiated by the container to decode
     * incoming messages into the expected custom objects on MessageHandler.Whole.onMessage(Object) callbacks.
     * 
     * @return the decoder implementation classes, the empty list if none.
     */
    List<Class<? extends Decoder>> getDecoders();

    /**
     * This method returns a modifiable Map that the developer may use to store application specific information
     * relating to the endpoint that uses this configuration instance. Web socket applications running on distributed
     * implementations of the web container should make any application specific objects stored here
     * java.io.Serializable, or the object may not be recreated after a failover.
     * 
     * @return a modifiable Map of application data.
     */
    Map<String, Object> getUserProperties();
}
