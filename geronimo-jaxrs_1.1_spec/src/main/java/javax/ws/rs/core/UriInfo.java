/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */

package javax.ws.rs.core;

import java.net.URI;
import java.util.List;

public interface UriInfo {
    public URI getAbsolutePath();

    public UriBuilder getAbsolutePathBuilder();

    public URI getBaseUri();

    public UriBuilder getBaseUriBuilder();

    public List<Object> getMatchedResources();

    public List<String> getMatchedURIs();

    public List<String> getMatchedURIs(boolean decode);

    public String getPath();

    public String getPath(boolean decode);

    public MultivaluedMap<String, String> getPathParameters();

    public MultivaluedMap<String, String> getPathParameters(boolean decode);

    public List<PathSegment> getPathSegments();

    public List<PathSegment> getPathSegments(boolean decode);

    public MultivaluedMap<String, String> getQueryParameters();

    public MultivaluedMap<String, String> getQueryParameters(boolean decode);

    public URI getRequestUri();

    public UriBuilder getRequestUriBuilder();
}
