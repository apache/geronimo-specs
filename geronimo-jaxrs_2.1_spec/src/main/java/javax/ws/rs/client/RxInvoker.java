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

public interface RxInvoker<T> {

    public T get();

    public <R> T get(Class<R> responseType);

    public <R> T get(GenericType<R> responseType);

    public T put(Entity<?> entity);

    public <R> T put(Entity<?> entity, Class<R> responseType);

    public <R> T put(Entity<?> entity, GenericType<R> responseType);

    public T post(Entity<?> entity);

    public <R> T post(Entity<?> entity, Class<R> responseType);

    public <R> T post(Entity<?> entity, GenericType<R> responseType);

    public T delete();

    public <R> T delete(Class<R> responseType);

    public <R> T delete(GenericType<R> responseType);

    public T head();

    public T options();

    public <R> T options(Class<R> responseType);

    public <R> T options(GenericType<R> responseType);

    public T trace();

    public <R> T trace(Class<R> responseType);

    public <R> T trace(GenericType<R> responseType);

    public T method(String name);

    public <R> T method(String name, Class<R> responseType);

    public <R> T method(String name, GenericType<R> responseType);

    public T method(String name, Entity<?> entity);

    public <R> T method(String name, Entity<?> entity, Class<R> responseType);

    public <R> T method(String name, Entity<?> entity, GenericType<R> responseType);
}
