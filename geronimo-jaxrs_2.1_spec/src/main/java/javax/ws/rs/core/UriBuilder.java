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

import javax.ws.rs.ext.RuntimeDelegate;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Map;


public abstract class UriBuilder {


    protected UriBuilder() {
    }


    protected static UriBuilder newInstance() {
        return RuntimeDelegate.getInstance().createUriBuilder();
    }


    public static UriBuilder fromUri(URI uri) {
        return newInstance().uri(uri);
    }


    public static UriBuilder fromUri(String uriTemplate) {
        return newInstance().uri(uriTemplate);
    }


    public static UriBuilder fromLink(Link link) {
        if (link == null) {
            throw new IllegalArgumentException("The provider 'link' parameter value is 'null'.");
        }
        return UriBuilder.fromUri(link.getUri());
    }


    public static UriBuilder fromPath(String path) throws IllegalArgumentException {
        return newInstance().path(path);
    }


    public static UriBuilder fromResource(Class<?> resource) {
        return newInstance().path(resource);
    }


    public static UriBuilder fromMethod(Class<?> resource, String method) {
        return newInstance().path(resource, method);
    }


    @SuppressWarnings("CloneDoesntDeclareCloneNotSupportedException")
    @Override
    public abstract UriBuilder clone();


    public abstract UriBuilder uri(URI uri);


    public abstract UriBuilder uri(String uriTemplate);


    public abstract UriBuilder scheme(String scheme);


    public abstract UriBuilder schemeSpecificPart(String ssp);


    public abstract UriBuilder userInfo(String ui);


    public abstract UriBuilder host(String host);


    public abstract UriBuilder port(int port);


    public abstract UriBuilder replacePath(String path);


    public abstract UriBuilder path(String path);


    public abstract UriBuilder path(Class resource);


    public abstract UriBuilder path(Class resource, String method);


    public abstract UriBuilder path(Method method);


    public abstract UriBuilder segment(String... segments);


    public abstract UriBuilder replaceMatrix(String matrix);


    public abstract UriBuilder matrixParam(String name, Object... values);


    public abstract UriBuilder replaceMatrixParam(String name, Object... values);


    public abstract UriBuilder replaceQuery(String query);


    public abstract UriBuilder queryParam(String name, Object... values);


    public abstract UriBuilder replaceQueryParam(String name, Object... values);


    public abstract UriBuilder fragment(String fragment);


    public abstract UriBuilder resolveTemplate(String name, Object value);


    public abstract UriBuilder resolveTemplate(String name, Object value, boolean encodeSlashInPath);


    public abstract UriBuilder resolveTemplateFromEncoded(String name, Object value);


    public abstract UriBuilder resolveTemplates(Map<String, Object> templateValues);


    public abstract UriBuilder resolveTemplates(Map<String, Object> templateValues, boolean encodeSlashInPath) throws IllegalArgumentException;


    public abstract UriBuilder resolveTemplatesFromEncoded(Map<String, Object> templateValues);


    public abstract URI buildFromMap(Map<String, ?> values);


    public abstract URI buildFromMap(Map<String, ?> values, boolean encodeSlashInPath) throws IllegalArgumentException, UriBuilderException;


    public abstract URI buildFromEncodedMap(Map<String, ?> values) throws IllegalArgumentException, UriBuilderException;


    public abstract URI build(Object... values) throws IllegalArgumentException, UriBuilderException;


    public abstract URI build(Object[] values, boolean encodeSlashInPath) throws IllegalArgumentException, UriBuilderException;


    public abstract URI buildFromEncoded(Object... values) throws IllegalArgumentException, UriBuilderException;


    public abstract String toTemplate();
}
