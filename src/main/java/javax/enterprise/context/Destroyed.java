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

import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Qualifier for events which get fired when a Context ends.
 * The exact point is before the contextual instances of that Context
 * actually get destroyed.
 *
 * Extensions should use a reasonable event object.
 * For built-in scopes the following event-classes will be used
 * <ul>
 *     <li>&#064;RequestScoped: the ServletRequest for web requests, any other Object for other 'requests'</li>
 *     <li>&#064;SessionScoped: the HttpSession</li>
 *     <li>&#064;ApplicationScoped: ServletContext for web apps, any other Object for other apps</li>
 *     <li>&#064;ConversationScoped: ServletRequest if handled during a web request, or any other Object for </li>
 * </ul>
 *
 * @see javax.enterprise.context.Initialized
 * @since 1.1
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier
public @interface Destroyed
{
    /**
     * @return the Scope annotation this is for.
     */
    Class<? extends Annotation> value();
}
