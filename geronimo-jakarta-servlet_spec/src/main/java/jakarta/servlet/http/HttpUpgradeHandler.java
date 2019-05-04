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

package javax.servlet.http;

/**
 * 
 * This interface encapsulates the upgrade protocol processing. A HttpUpgradeHandler implementation would allow the
 * servlet container to communicate with it.
 * 
 * @since Servlet 3.1
 */
public interface HttpUpgradeHandler {

    /**
     * It is called when the client is disconnected.
     */
    void destroy();

    /**
     * It is called once the HTTP Upgrade process has been completed and the upgraded connection is ready to start using
     * the new protocol.
     * 
     * @param wc
     *            - the WebConnection object associated to this upgrade request
     */
    void init(WebConnection wc);
}
