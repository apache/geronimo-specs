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

package javax.ws.rs.ext;

import javax.ws.rs.core.MediaType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;


public interface InterceptorContext {


    Object getProperty(String name);


    Collection<String> getPropertyNames();


    void setProperty(String name, Object object);


    void removeProperty(String name);


    Annotation[] getAnnotations();


    void setAnnotations(Annotation[] annotations);


    Class<?> getType();


    void setType(Class<?> type);


    Type getGenericType();


    void setGenericType(Type genericType);


    MediaType getMediaType();


    void setMediaType(MediaType mediaType);
}
