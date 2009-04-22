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

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @version $Rev$ $Date$
 */
@Target( { FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Documented
public @interface Pattern {

    public static enum Flag {
        /**
         * @see java.util.regex.Pattern#UNIX_LINES
         */
        UNIX_LINES,

        /**
         * @see java.util.regex.Pattern#CASE_INSENSITIVE
         */
        CASE_INSENSITIVE,

        /**
         * @see java.util.regex.Pattern#COMMENTS
         */
        COMMENTS,

        /**
         * @see java.util.regex.Pattern#MULTILINE
         */
        MULTILINE,

        /**
         * @see java.util.regex.Pattern#DOTALL
         */
        DOTALL,

        /**
         * @see java.util.regex.Pattern#UNICODE_CASE
         */
        UNICODE_CASE,

        /**
         * @see java.util.regex.Pattern#CANON_EQ
         */
        CANON_EQ
    }

    /**
     * Pattern[]
     */
    @Target( { FIELD, METHOD, ANNOTATION_TYPE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        Pattern[] value();
    }

    /**
     * @return Flag[]
     */
    Flag[] flags() default {};

    /**
     * @return Class[]
     */
    Class<?>[] groups() default {};

    /**
     * Default message interpolation
     * 
     * @return String
     */
    String message() default "{constraint.pattern}";

    /**
     * @return String
     */
    String regexp();
}
