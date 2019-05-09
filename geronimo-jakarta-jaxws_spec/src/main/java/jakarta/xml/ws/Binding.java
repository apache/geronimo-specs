/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package jakarta.xml.ws;

import jakarta.xml.ws.handler.Handler;
import java.util.List;

public interface Binding {

    /**
     * Get the BindingId of this binding instance
     * @return
     */
    public abstract String getBindingID();
    
    /**
     * Get the list of handlers that are associated with this binding instance
     * @return
     */
    public abstract List<Handler> getHandlerChain();

    /**
     * Set's the list of handlers for this binding instance
     * @param list
     */
    public abstract void setHandlerChain(List<Handler> list);
}
