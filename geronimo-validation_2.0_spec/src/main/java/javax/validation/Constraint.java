/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package javax.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Meta annotation to declare a constraint annotation.
 *
 * Every custom constraint annotation meta-annotated with this very annotation must implement the following fields:
 * <ul>
 *     <li>{@code String message()} - the message to be used if this constraint is violated.</li>
 *     <li>{@code Class<?>[] groups() default {};} - the validation group. See {@link GroupSequence}.
 *         If a constraint gets applied without any specific group then {@link javax.validation.groups.Default} is assumed.
 *     </li>
 *     <li>{@code Class<? extends Payload>[] payload() default {};} - Custom {@link Payload} for the contstraint.</li>
 * </ul>
 *
 * @see javax.validation.constraints Built in Constraints
 *
 * @version $Rev$ $Date$
 */
@Documented
@Target({ ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface Constraint {

    /**
     * @return the {@link ConstraintValidator} which gets used when this constraint is applied
     *          to fields, methods (getters), types or parameter.
     */
    Class<? extends ConstraintValidator<?,?>>[] validatedBy();
}

