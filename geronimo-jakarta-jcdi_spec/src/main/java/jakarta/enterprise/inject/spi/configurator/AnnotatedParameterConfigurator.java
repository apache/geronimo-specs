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

import java.lang.annotation.Annotation;
import java.util.function.Predicate;

import jakarta.enterprise.inject.spi.AnnotatedParameter;

public interface AnnotatedParameterConfigurator<T> {

    /**
     *
     * @return the underlying parameter.
     */
    AnnotatedParameter<T> getAnnotated();

    /**
     * Adds an instance of the given annotation to the parameter.
     *
     * @param annotation
     * @return self
     */
    AnnotatedParameterConfigurator<T> add(Annotation annotation);

    /**
     * Removes all Annotations which fit the given Predicate
     * @param annotation
     * @return self
     */
    AnnotatedParameterConfigurator<T> remove(Predicate<Annotation> annotation);

    /**
     * removes all Annotations
     * @return self
     */
    default AnnotatedParameterConfigurator<T> removeAll()
    {
        remove((e) -> true);
        return this;
    }

}