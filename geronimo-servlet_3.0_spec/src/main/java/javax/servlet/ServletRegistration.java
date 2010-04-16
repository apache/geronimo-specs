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

import java.util.Collection;
import java.util.Set;

/**
 * @version $Rev$ $Date$
 * @since 3.0
 */
public interface ServletRegistration extends Registration {

    /**
     *
     * @param urlPatterns patterns to map
     * @return patterns already mapped to another servlet
     */
    Set<String> addMapping(String... urlPatterns);

    Collection<String> getMappings();

    String getRunAsRole();

    public interface Dynamic extends ServletRegistration, Registration.Dynamic {

        void setLoadOnStartup(int loadOnStartup);

        void setMultipartConfig(MultipartConfigElement element);

        void setRunAsRole(String role);

        /**
         *
         * @param securityElement
         * @return set of url mappings that were not changed
         */
        Set<String> setServletSecurity(ServletSecurityElement securityElement);
        
    }
}
