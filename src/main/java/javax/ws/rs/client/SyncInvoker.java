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

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

public interface SyncInvoker {

    // GET

    Response get();

    <T> T get(Class<T> responseType);

    <T> T get(GenericType<T> responseType);

    // PUT

    Response put(Entity<?> entity);

    <T> T put(Entity<?> entity, Class<T> responseType);

    <T> T put(Entity<?> entity, GenericType<T> responseType);

    // POST

    Response post(Entity<?> entity);

    <T> T post(Entity<?> entity, Class<T> responseType);

    <T> T post(Entity<?> entity, GenericType<T> responseType);

    // DELETE

    Response delete();

    <T> T delete(Class<T> responseType);

    <T> T delete(GenericType<T> responseType);

    // HEAD

    Response head();

    // OPTIONS

    Response options();

    <T> T options(Class<T> responseType);

    <T> T options(GenericType<T> responseType);

    // TRACE

    Response trace();

    <T> T trace(Class<T> responseType);

    <T> T trace(GenericType<T> responseType);

    // OTHERS

    Response method(String name);

    <T> T method(String name, Class<T> responseType);

    <T> T method(String name, GenericType<T> responseType);

    Response method(String name, Entity<?> entity);

    <T> T method(String name, Entity<?> entity, Class<T> responseType);

    <T> T method(String name, Entity<?> entity, GenericType<T> responseType);
}
