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
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package javax.ws.rs.client;

import java.util.Locale;
import java.util.concurrent.Future;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public interface Invocation {

    public static interface Builder extends SyncInvoker {

        // Invocation builder methods

        public Invocation build(String method);

        public Invocation build(String method, Entity<?> entity);

        public Invocation buildGet();

        public Invocation buildDelete();

        public Invocation buildPost(Entity<?> entity);

        public Invocation buildPut(Entity<?> entity);

        public AsyncInvoker async();

        public Builder accept(String... mediaTypes);

        public Builder accept(MediaType... mediaTypes);

        public Builder acceptLanguage(Locale... locales);

        public Builder acceptLanguage(String... locales);

        public Builder acceptEncoding(String... encodings);

        public Builder cookie(Cookie cookie);

        public Builder cookie(String name, String value);

        public Builder cacheControl(CacheControl cacheControl);

        public Builder header(String name, Object value);

        public Builder headers(MultivaluedMap<String, Object> headers);

        public Builder property(String name, Object value);

        public CompletionStageRxInvoker rx();

        public <T extends RxInvoker> T rx(Class<T> clazz);

    }

    public Invocation property(String name, Object value);

    public Response invoke();

    public <T> T invoke(Class<T> responseType);

    public <T> T invoke(GenericType<T> responseType);

    public Future<Response> submit();

    public <T> Future<T> submit(Class<T> responseType);

    public <T> Future<T> submit(GenericType<T> responseType);

    public <T> Future<T> submit(InvocationCallback<T> callback);
}
