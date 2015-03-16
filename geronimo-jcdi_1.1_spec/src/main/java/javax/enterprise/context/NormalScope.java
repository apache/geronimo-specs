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
package javax.enterprise.context;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>Defines CDI scopes which have a well-defined lifecycle. Examples for such scopes
 * are {@link javax.enterprise.context.RequestScoped}, {@link javax.enterprise.context.SessionScoped}
 * and {@link javax.enterprise.context.ApplicationScoped}.</p>
 * <p>Beans of such a scope will get a normalscoping proxy (Contextual Reference)
 * for every injection.</p>
 *
 * <p>If a NormalScope is {@code passivating} then all it's Contextual Instances need
 * to implement {@code java.io.Serializable}.</p>
 * @version $Rev$ $Date$
 *
 */
@Target(ANNOTATION_TYPE)
@Retention(RUNTIME)
@Documented
public @interface NormalScope
{
    /**Defines passivation semantic*/
    boolean passivating() default false;
}
