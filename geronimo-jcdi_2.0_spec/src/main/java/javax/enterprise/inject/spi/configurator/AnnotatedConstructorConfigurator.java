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

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedParameter;

public interface AnnotatedConstructorConfigurator<T> {

    /**
     * @return the underlying constructor
     */
    AnnotatedConstructor<T> getAnnotated();

    /**
     * Adds an instance of the given annotation to the constructor.
     *
     * @param annotation
     * @return this
     */
    AnnotatedConstructorConfigurator<T> add(Annotation annotation);

    /**
     * Removes all Annotations which fit the given Predicate
     * @param annotation
     * @return self
     */
    AnnotatedConstructorConfigurator<T> remove(Predicate<Annotation> annotation);

    /**
     * removes all Annotations
     * @return self
     */
    default AnnotatedConstructorConfigurator<T> removeAll()
    {
        remove((e) -> true);
        return this;
    }

    /**
     *
     * @return the parameters for the constructor.  Need not be mutable.
     */
    List<AnnotatedParameterConfigurator<T>> params();

   /**
    *
    * @param predicate the filter to apply
    * @return a stream representation of the underlying
    */
    default Stream<AnnotatedParameterConfigurator<T>> filterParams(Predicate<AnnotatedParameter<T>> predicate)
    {
        return params()
                .stream()
                .filter(ap -> predicate.test(ap.getAnnotated()));
    }

}
