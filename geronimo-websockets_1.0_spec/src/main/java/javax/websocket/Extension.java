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

package javax.websocket;

import java.util.List;

/**
 * A simple representation of a websocket extension as a name and map of extension parameters.
 */
public interface Extension {

    /**
     * This member interface defines a single websocket extension parameter.
     */
    public static interface Parameter {
        /**
         * Return the name of the extension parameter.
         * 
         * @return the name of the parameter.
         */
        String getName();

        /**
         * Return the value of the extension parameter.
         * 
         * @return the value of the parameter.
         */
        String getValue();
    }

    /**
     * The name of the extension.
     * 
     * @return the name of the extension.
     */
    String getName();

    /**
     * The extension parameters for this extension in the order they appear in the http headers.
     * 
     * @return The read-only Map of extension parameters belonging to this extension.
     */
    List<Parameter> getParameters();
}
