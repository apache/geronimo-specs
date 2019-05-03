/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package jakarta.enterprise.inject.spi.configurator;

import jakarta.enterprise.context.spi.CreationalContext;
import java.util.function.Consumer;
import java.util.function.Function;

public interface ProducerConfigurator<T> {


    /**
     *
     *
     * @param callback use as produced instance for the configured bean
     * @return self
     */
    <U extends T> ProducerConfigurator<T> produceWith(Function<CreationalContext<U>, U> callback);

    /**
     *
     * Set a {@link Consumer} to destroy a bean instance.
     * If no dispose callback is specified, a NOOP dispose callback is automatically set.
     *
     * @param callback the Consumer to dispose the instance
     * @return self
     */
    ProducerConfigurator<T> disposeWith(Consumer<T> callback);
}
