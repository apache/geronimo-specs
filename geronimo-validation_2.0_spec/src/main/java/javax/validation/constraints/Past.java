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
package javax.validation.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>
 * Verify that the annotated value of the annotated element is in the past or {@code null}.
 * </p>
 * <p>
 * Supports:
 * <ul>
 * <li>{@code java.util.Date}</li>
 * <li>{@code java.util.Calendar}</li>
 * <li>{@code java.time.Instant}</li>
 * <li>{@code java.time.LocalDate}</li>
 * <li>{@code java.time.LocalDateTime}</li>
 * <li>{@code java.time.LocalTime}</li>
 * <li>{@code java.time.MonthDay}</li>
 * <li>{@code java.time.OffsetDateTime}</li>
 * <li>{@code java.time.OffsetTime}</li>
 * <li>{@code java.time.Year}</li>
 * <li>{@code java.time.YearMonth}</li>
 * <li>{@code java.time.ZonedDateTime}</li>
 * <li>{@code java.time.chrono.HijrahDate}</li>
 * <li>{@code java.time.chrono.JapaneseDate}</li>
 * <li>{@code java.time.chrono.MinguoDate}</li>
 * <li>{@code java.time.chrono.ThaiBuddhistDate}</li>
 * </ul>
 * </p>
 *
 * Other types might be supported in a non-portable manner.
 *
 * @version $Rev$ $Date$
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {})
@Repeatable(Past.List.class)
public @interface Past {
    String message() default "{javax.validation.constraints.Past.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        Past[] value();
    }
}
