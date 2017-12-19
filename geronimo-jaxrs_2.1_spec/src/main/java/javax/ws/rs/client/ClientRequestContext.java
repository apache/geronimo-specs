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

package javax.ws.rs.client;

import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public interface ClientRequestContext {


    Object getProperty(String name);


    Collection<String> getPropertyNames();


    void setProperty(String name, Object object);


    void removeProperty(String name);


    URI getUri();


    void setUri(URI uri);


    String getMethod();


    void setMethod(String method);


    MultivaluedMap<String, Object> getHeaders();


    abstract MultivaluedMap<String, String> getStringHeaders();


    String getHeaderString(String name);


    Date getDate();


    Locale getLanguage();


    MediaType getMediaType();


    List<MediaType> getAcceptableMediaTypes();


    List<Locale> getAcceptableLanguages();


    Map<String, Cookie> getCookies();


    boolean hasEntity();


    Object getEntity();


    Class<?> getEntityClass();


    Type getEntityType();


    void setEntity(final Object entity);


    void setEntity(final Object entity, final Annotation[] annotations, final MediaType mediaType);


    Annotation[] getEntityAnnotations();


    OutputStream getEntityStream();


    void setEntityStream(OutputStream outputStream);


    Client getClient();


    Configuration getConfiguration();


    void abortWith(Response response);
}
