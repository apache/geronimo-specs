/*
 * #%L
 * Apache Geronimo JAX-RS Spec 2.0
 * %%
 * Copyright (C) 2003 - 2014 The Apache Software Foundation
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package javax.ws.rs.core;

import java.net.URI;
import java.util.List;


public interface UriInfo {


    public String getPath();


    public String getPath(boolean decode);


    public List<PathSegment> getPathSegments();


    public List<PathSegment> getPathSegments(boolean decode);


    public URI getRequestUri();


    public UriBuilder getRequestUriBuilder();


    public URI getAbsolutePath();


    public UriBuilder getAbsolutePathBuilder();


    public URI getBaseUri();


    public UriBuilder getBaseUriBuilder();


    public MultivaluedMap<String, String> getPathParameters();


    public MultivaluedMap<String, String> getPathParameters(boolean decode);


    public MultivaluedMap<String, String> getQueryParameters();


    public MultivaluedMap<String, String> getQueryParameters(boolean decode);


    public List<String> getMatchedURIs();


    public List<String> getMatchedURIs(boolean decode);


    public List<Object> getMatchedResources();


    public URI resolve(URI uri);


    public URI relativize(URI uri);

}
