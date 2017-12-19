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

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.Locale;
import java.util.concurrent.Future;


public interface Invocation {


    static interface Builder extends SyncInvoker {


        Invocation build(String method);


        Invocation build(String method, Entity<?> entity);


        Invocation buildGet();


        Invocation buildDelete();


        Invocation buildPost(Entity<?> entity);


        Invocation buildPut(Entity<?> entity);


        AsyncInvoker async();


        Builder accept(String... mediaTypes);


        Builder accept(MediaType... mediaTypes);


        Builder acceptLanguage(Locale... locales);


        Builder acceptLanguage(String... locales);


        Builder acceptEncoding(String... encodings);


        Builder cookie(Cookie cookie);


        Builder cookie(String name, String value);


        Builder cacheControl(CacheControl cacheControl);


        Builder header(String name, Object value);


        Builder headers(MultivaluedMap<String, Object> headers);


        Builder property(String name, Object value);
    }


    Invocation property(String name, Object value);


    Response invoke();


    <T> T invoke(Class<T> responseType);


    <T> T invoke(GenericType<T> responseType);


    Future<Response> submit();


    <T> Future<T> submit(Class<T> responseType);


    <T> Future<T> submit(GenericType<T> responseType);


    <T> Future<T> submit(InvocationCallback<T> callback);
}
