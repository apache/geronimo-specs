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

package javax.enterprise.inject.spi.configurator;

import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.util.Nonbinding;
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
     * Remove annotations with (a) the same type and (b) the same annotation member value for each member which is not
     * annotated {@link Nonbinding}. The container calls the {@link Object#equals(Object)} method of the annotation member value
     * to compare values.
     *
     * @param annotation to remove
     * @return self
     */
    AnnotatedMethodConfigurator<T> remove(Annotation annotation);

    /**
     * Removes all annotations with the same type. Annotation members are ignored.
     *
     * @param annotationType annotation class to remove
     * @return self
     */
    AnnotatedMethodConfigurator<T> remove(Class<? extends Annotation> annotationType);

    /**
     * Remove all annotations from the method.
     *
     * @return self
     */
    AnnotatedMethodConfigurator<T> removeAll();

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
    Stream<AnnotatedParameterConfigurator<T>> filterParams(Predicate<AnnotatedParameter<T>> predicate);

}