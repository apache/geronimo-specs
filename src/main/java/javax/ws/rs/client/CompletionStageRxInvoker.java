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

import java.util.concurrent.CompletionStage;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

public interface CompletionStageRxInvoker extends RxInvoker<CompletionStage> {

    @Override
    public CompletionStage<Response> get();

    @Override
    public <T> CompletionStage<T> get(Class<T> responseType);

    @Override
    public <T> CompletionStage<T> get(GenericType<T> responseType);

    @Override
    public CompletionStage<Response> put(Entity<?> entity);

    @Override
    public <T> CompletionStage<T> put(Entity<?> entity, Class<T> clazz);

    @Override
    public <T> CompletionStage<T> put(Entity<?> entity, GenericType<T> type);

    @Override
    public CompletionStage<Response> post(Entity<?> entity);

    @Override
    public <T> CompletionStage<T> post(Entity<?> entity, Class<T> clazz);

    @Override
    public <T> CompletionStage<T> post(Entity<?> entity, GenericType<T> type);

    @Override
    public CompletionStage<Response> delete();

    @Override
    public <T> CompletionStage<T> delete(Class<T> responseType);

    @Override
    public <T> CompletionStage<T> delete(GenericType<T> responseType);

    @Override
    public CompletionStage<Response> head();

    @Override
    public CompletionStage<Response> options();

    @Override
    public <T> CompletionStage<T> options(Class<T> responseType);

    @Override
    public <T> CompletionStage<T> options(GenericType<T> responseType);

    @Override
    public CompletionStage<Response> trace();

    @Override
    public <T> CompletionStage<T> trace(Class<T> responseType);

    @Override
    public <T> CompletionStage<T> trace(GenericType<T> responseType);

    @Override
    public CompletionStage<Response> method(String name);

    @Override
    public <T> CompletionStage<T> method(String name, Class<T> responseType);

    @Override
    public <T> CompletionStage<T> method(String name, GenericType<T> responseType);

    @Override
    public CompletionStage<Response> method(String name, Entity<?> entity);

    @Override
    public <T> CompletionStage<T> method(String name, Entity<?> entity, Class<T> responseType);

    @Override
    public <T> CompletionStage<T> method(String name, Entity<?> entity, GenericType<T> responseType);
}

