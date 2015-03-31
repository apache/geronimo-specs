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
package javax.interceptor;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation for used defining the interceptors.
 */
@Retention(RUNTIME)
@Target({TYPE})
@Documented
public @interface Interceptor
{
    /**
     * A set of default priorities which can be used by
     * Interceptors via {@code &#064;javax.annotation.Priority}.
     */
    public static class Priority {
        public static final int PLATFORM_BEFORE = 0;
        public static final int LIBRARY_BEFORE = 1000;
        public static final int APPLICATION = 2000;
        public static final int LIBRARY_AFTER = 3000;
        public static final int PLATFORM_AFTER = 4000;

        private Priority() {
            // no-op
        }
    }
}
