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
import java.lang.reflect.Type;
import java.util.Set;

public interface InjectionPointConfigurator {

    /**
     * Set the required {@link Type} (that will be used during typesafe resolution)
     * of the InjectionPoint to build.
     *
     * @param requiredType for the InjectionPoint to build
     * @return self
     */
    InjectionPointConfigurator type(Type requiredType);

    /**
     *
     * Add the qualifier to the InjectionPoint to build
     *
     * @param qualifier the qualifier to add
     * @return self
     */
    InjectionPointConfigurator addQualifier(Annotation qualifier);

    /**
     *
     * Add all the qualifiers to the InjectionPoint to build
     *
     * @param qualifiers a varargs or array of qualifiers to add
     * @return self
     */
    InjectionPointConfigurator addQualifiers(Annotation... qualifiers);

    /**
     *
     * Add all the qualifiers to the InjectionPoint to build
     *
     * @param qualifiers a Set of qualifiers to add
     * @return self
     */
    InjectionPointConfigurator addQualifiers(Set<Annotation> qualifiers);

    /**
     * Replace all qualifiers.
     *
     * @param qualifiers a varargs or array of qualifiers to replace to existing ones
     * @return self
     */
    InjectionPointConfigurator qualifiers(Annotation... qualifiers);

    /**
     * Replace all qualifiers.
     *
     * @param qualifiers a Set of qualifiers to replace to existing ones
     * @return self
     */
    InjectionPointConfigurator qualifiers(Set<Annotation> qualifiers);

    /**
     *
     * Change the delegate status of the built InjectionPoint.
     * By default the InjectionPoint is not a delegate one.
     *
     * @param delegate boolean to define or undefine the delegate nature of the configured InjectionPoint
     * @return self
     */
    InjectionPointConfigurator delegate(boolean delegate);

    /**
     *
     * Change the transient status of the built InjectionPoint.
     * By default the InjectionPoint is not transient.
     *
     * @param trans boolean to define or undefine the transient nature of the configured InjectionPoint
     * @return self
     */
    InjectionPointConfigurator transientField(boolean trans);
}
