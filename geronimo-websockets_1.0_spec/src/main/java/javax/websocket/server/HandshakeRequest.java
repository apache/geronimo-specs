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

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * The handshake request represents the web socket defined Http GET request for the opening handshake of a web socket
 * session.
 */
public interface HandshakeRequest {

    /**
     * The Sec-WebSocket-Extensions header name
     */
    static final String SEC_WEBSOCKET_EXTENSIONS = "Sec-WebSocket-Extensions";

    /**
     * The Sec-WebSocket-Key header name
     */
    static final String SEC_WEBSOCKET_KEY = "Sec-WebSocket-Key";

    /**
     * The Sec-WebSocket-Protocol header name
     */
    static final String SEC_WEBSOCKET_PROTOCOL = "Sec-WebSocket-Protocol";

    /**
     * The Sec-WebSocket-Version header name
     */
    static final String SEC_WEBSOCKET_VERSION = "Sec-WebSocket-Version";

    /**
     * Return the read only Map of Http Headers that came with the handshake request. The header names are case
     * insensitive.
     * 
     * @return the list of headers
     */
    Map<String, List<String>> getHeaders();

    /**
     * Return a reference to the HttpSession that the web socket handshake that started this conversation was part of,
     * if the implementation is part of a Java EE web container.
     * 
     * @return the http session or null if either the websocket implementation is not part of a Java EE web container,
     *         or there is no HttpSession associated with the opening handshake request.
     */
    Object getHttpSession();

    /**
     * Return the request parameters associated with the request.
     * 
     * @return the unmodifiable map of the request parameters.
     */
    Map<String, List<String>> getParameterMap();

    /**
     * Return the query string associated with the request.
     * 
     * @return the query string
     */
    String getQueryString();

    /**
     * Return the request URI of the handshake request.
     * 
     * @return the request uri of the handshake request.
     */
    URI getRequestURI();

    /**
     * Return the authenticated user or null if no user is authenticated for this handshake.
     * 
     * @return the user principal
     */
    Principal getUserPrincipal();

    /**
     * Checks whether the current user is in the given role. Roles and role membership can be defined using deployment
     * descriptors of the containing WAR file, if running in a Java EE web container. If the user has not been
     * authenticated, the method returns false.
     * 
     * @param role
     *            the role being checked.
     * @return whether the authenticated user is in the role, or false if the user has not been authenticated.
     */
    boolean isUserInRole(String role);

}
