/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package javax.enterprise.inject.spi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * Decorator Bean.
 */
public interface Decorator<T> extends Bean<T>
{

    /**
     * All the interfaces and classes in the type hierarchy of the
     * class annotated with &#064;{@link javax.decorator.Decorator}.
     * @return the decorated types of the decorator.
     */
    Set<Type> getDecoratedTypes();

    /**
     * @return the Type of the &#064;{@link javax.decorator.Delegate} injection point.
     */
    Type getDelegateType();

    /**
     * @return the Qualifiers of the &#064;{@link javax.decorator.Delegate} injection point.
     */
    Set<Annotation> getDelegateQualifiers();

}
