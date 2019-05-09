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

import java.util.concurrent.Future;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

public interface AsyncInvoker {

    // GET

    Future<Response> get();

    <T> Future<T> get(Class<T> responseType);

    <T> Future<T> get(GenericType<T> responseType);

    <T> Future<T> get(InvocationCallback<T> callback);

    // PUT

    Future<Response> put(Entity<?> entity);

    <T> Future<T> put(Entity<?> entity, Class<T> responseType);

    <T> Future<T> put(Entity<?> entity, GenericType<T> responseType);

    <T> Future<T> put(Entity<?> entity, InvocationCallback<T> callback);

    // POST

    Future<Response> post(Entity<?> entity);

    <T> Future<T> post(Entity<?> entity, Class<T> responseType);

    <T> Future<T> post(Entity<?> entity, GenericType<T> responseType);

    <T> Future<T> post(Entity<?> entity, InvocationCallback<T> callback);

    // DELETE

    Future<Response> delete();

    <T> Future<T> delete(Class<T> responseType);

    <T> Future<T> delete(GenericType<T> responseType);

    <T> Future<T> delete(InvocationCallback<T> callback);

    // HEAD

    Future<Response> head();

    Future<Response> head(InvocationCallback<Response> callback);

    // OPTIONS

    Future<Response> options();

    <T> Future<T> options(Class<T> responseType);

    <T> Future<T> options(GenericType<T> responseType);

    <T> Future<T> options(InvocationCallback<T> callback);

    // TRACE

    Future<Response> trace();

    <T> Future<T> trace(Class<T> responseType);

    <T> Future<T> trace(GenericType<T> responseType);

    <T> Future<T> trace(InvocationCallback<T> callback);

    // OTHERS

    Future<Response> method(String name);

    <T> Future<T> method(String name, Class<T> responseType);

    <T> Future<T> method(String name, GenericType<T> responseType);

    <T> Future<T> method(String name, InvocationCallback<T> callback);

    Future<Response> method(String name, Entity<?> entity);

    <T> Future<T> method(String name, Entity<?> entity, Class<T> responseType);

    <T> Future<T> method(String name, Entity<?> entity, GenericType<T> responseType);

    <T> Future<T> method(String name, Entity<?> entity, InvocationCallback<T> callback);
}
