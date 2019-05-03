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

import jakarta.enterprise.inject.spi.AnnotatedMethod;
import jakarta.enterprise.inject.spi.AnnotatedParameter;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface AnnotatedMethodConfigurator<T> {

    /**
     *
     * @return the original {@link AnnotatedMethod}
     */
    AnnotatedMethod<T> getAnnotated();

    /**
     * Add an annotation to the field.
     *
     * @param annotation to add
     * @return self
     */
    AnnotatedMethodConfigurator<T> add(Annotation annotation);

    /**
     * Removes all Annotations which fit the given Predicate
     * @param annotation
     * @return self
     */
    AnnotatedMethodConfigurator<T> remove(Predicate<Annotation> annotation);

    /**
     * removes all Annotations
     * @return self
     */
    default AnnotatedMethodConfigurator<T> removeAll()
    {
        remove((e) -> true);
        return this;
    }

    /**
     *
     * @return an immutable list of {@link AnnotatedParameterConfigurator}s reflecting the
     *         {@link AnnotatedMethod#getParameters()}
     */
    List<AnnotatedParameterConfigurator<T>> params();

    /**
     *
     * @param predicate Testing the original {@link AnnotatedParameter}
     * @return a sequence of {@link AnnotatedParameterConfigurator}s matching the given predicate
     * @see AnnotatedParameterConfigurator#getAnnotated()
     */
    default Stream<AnnotatedParameterConfigurator<T>> filterParams(Predicate<AnnotatedParameter<T>> predicate)
    {
        return params()
                .stream()
                .filter(ap -> predicate.test(ap.getAnnotated()));
    }

}