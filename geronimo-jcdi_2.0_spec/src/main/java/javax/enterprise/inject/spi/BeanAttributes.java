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
 * Defines the mutable parts of the {@link Bean} interface.
 *
 * @since 1.1‚
 */
public interface BeanAttributes<T> {
    /**
     * Returns api types of a bean.
     *
     * @return api types of a bean
     */
    public abstract Set<Type> getTypes();

    /**
     * Returns qualifiers of a bean.
     *
     * @return qualifiers of a bean
     */
    public abstract Set<Annotation> getQualifiers();

    /**
     * Returns scope of a bean.
     *
     * @return scope
     */
    public abstract Class<? extends Annotation> getScope();

    /**
     * Returns name of a bean.
     *
     * @return name of a bean
     */
    public abstract String getName();

    /**
     * Returns bean stereotypes.
     *
     * @return bean stereotypes
     */
    public Set<Class<? extends Annotation>> getStereotypes();

    /**
     * Returns true if declares as policy
     *
     * @return true if declares as policy
     */
    public boolean isAlternative();
}
