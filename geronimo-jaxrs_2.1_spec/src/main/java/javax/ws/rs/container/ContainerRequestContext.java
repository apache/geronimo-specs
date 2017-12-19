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

package javax.ws.rs.container;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.io.InputStream;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public interface ContainerRequestContext {


    Object getProperty(String name);


    Collection<String> getPropertyNames();


    void setProperty(String name, Object object);


    void removeProperty(String name);


    UriInfo getUriInfo();


    void setRequestUri(URI requestUri);


    void setRequestUri(URI baseUri, URI requestUri);


    Request getRequest();


    String getMethod();


    void setMethod(String method);


    MultivaluedMap<String, String> getHeaders();


    String getHeaderString(String name);


    Date getDate();


    Locale getLanguage();


    int getLength();


    MediaType getMediaType();


    List<MediaType> getAcceptableMediaTypes();


    List<Locale> getAcceptableLanguages();


    Map<String, Cookie> getCookies();


    boolean hasEntity();


    InputStream getEntityStream();


    void setEntityStream(InputStream input);


    SecurityContext getSecurityContext();


    void setSecurityContext(SecurityContext context);


    void abortWith(Response response);
}
