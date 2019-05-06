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
 * The handshake response represents the web socket-defined Http response that is the response to the opening handshake
 * request.
 */
public interface HandshakeResponse {

    /**
     * The Sec-WebSocket-Accept header name.
     */
    static final String SEC_WEBSOCKET_ACCEPT = "Sec-WebSocket-Accept";

    /**
     * Return the list of Http headers sent by the web socket server.
     * 
     * @return the http headers .
     */
    Map<String, List<String>> getHeaders();
}
