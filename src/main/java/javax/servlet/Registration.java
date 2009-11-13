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


package javax.servlet;

import java.util.Map;
import java.util.Set;

/**
 * @version $Rev$ $Date$
 * @since 3.0
 */
public interface Registration {

    /**
     *
     * @return the class name of filter or servlet this registered
     */
    String getClassName();

    /**
     *
     * @param name init parameter name
     * @return init parameter value or null if not set
     */
    String getInitParameter(String name);

    /**
     *
     * @return non-null immutable map of all init parameters
     */
    Map<String, String> getInitParameters();

    /**
     *
     * @return name of filter or servlet this initialised
     */
    String getName();

    /**
     * Set one init parameter
     * @param name parameter name
     * @param value parameter value
     * @return true if init parameter was set, false if it was previously set.
     */
    boolean setInitParameter(String name, String value);

    /**
     *
     * @param initParameters map of name-value paramters
     * @return set of previously set parameter names (i.e. those where setInitParameter would have returned false)
     */
    Set<String> setInitParameters(Map<String, String> initParameters);

    public interface Dynamic extends Registration {

        void setAsyncSupported(boolean asyncSupported);

    }
}
