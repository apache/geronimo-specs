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

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.ws.rs.ext.RuntimeDelegate;

public abstract class UriBuilder {
    protected UriBuilder() {
        super();
    }

    public static UriBuilder fromPath(String value) {
        return newInstance().replacePath(value);
    }

    public static UriBuilder fromResource(Class<?> resourceClass) {
        return newInstance().path(resourceClass);
    }

    public static UriBuilder fromUri(String value) {
        try {
            return fromUri(new URI(value));
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(e);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static UriBuilder fromUri(URI uri) {
        return newInstance().uri(uri);
    }

    private final static RuntimeDelegate delegate = RuntimeDelegate.getInstance();

    protected static UriBuilder newInstance() {
        return delegate.createUriBuilder();
    }

    public abstract URI build(Object... values);

    public abstract URI buildFromEncoded(Object... values);

    public abstract URI buildFromEncodedMap(Map<String, ? extends Object> values);

    public abstract URI buildFromMap(Map<String, ? extends Object> values);

    @Override
    public abstract UriBuilder clone();

    public abstract UriBuilder fragment(String value);

    public abstract UriBuilder host(String value);

    public abstract UriBuilder matrixParam(String name, Object... values);

    public abstract UriBuilder path(Class resourceClass);

    public abstract UriBuilder path(Class resourceClass, String resourceMethodName);

    public abstract UriBuilder path(Method resourceMethod);

    public abstract UriBuilder path(String value);

    public abstract UriBuilder port(int port);

    public abstract UriBuilder queryParam(String name, Object... values);

    public abstract UriBuilder replaceMatrix(String value);

    public abstract UriBuilder replaceMatrixParam(String name, Object... values);

    public abstract UriBuilder replacePath(String value);

    public abstract UriBuilder replaceQuery(String value);

    public abstract UriBuilder replaceQueryParam(String name, Object... values);

    public abstract UriBuilder scheme(String value);

    public abstract UriBuilder schemeSpecificPart(String value);

    public abstract UriBuilder segment(String... values);

    public abstract UriBuilder uri(URI value);

    public abstract UriBuilder userInfo(String value);
}
