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
package jakarta.enterprise.inject;

import jakarta.enterprise.util.AnnotationLiteral;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks an injected parameter to be destroyed after invocation.
 *  
 * When a constructor or a method is annotated with {@link jakarta.inject.Inject},
 * its parameters may be annotated with <tt>\@TransientReference</tt>.
 * If a parameter is annotated with <tt>\@TransientReference</tt>
 * and the injected bean is {@link jakarta.enterprise.context.Dependent}-scoped,
 * it may be destroyed by the container right after the invocation
 * of the constructor or method.
 * 
 * Technically spoken, the parameter will not be added to the
 * {@link jakarta.enterprise.context.spi.CreationalContext}
 * of the bean of the constructor or method, but will be created with a separate
 * {@link jakarta.enterprise.context.spi.CreationalContext}
 * that will be destroyed after the invocation.
 */
@Target(value = PARAMETER)
@Retention(value = RUNTIME)
@Documented
public @interface TransientReference
{
    final class Literal extends AnnotationLiteral<TransientReference> implements TransientReference
    {
        public static final Literal INSTANCE = new Literal();

        private static final long serialVersionUID = 1L;
    }

}