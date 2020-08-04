/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package javax.mail;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used by Java EE applications to define a MailSession
 * to be registered within JNDI.
 * The annotation is used to configure the commonly used Session
 * properties.
 * Additional standard and vendor-specific properties may be
 * specified using the properties element.
 * 
 * The session will be registered under the JNDI name specified in the
 * name element.  It may be defined to be in any valid
 * Java EE namespace, and will determine the accessibility of
 * the session from other components.
 *
 * @since JavaMail 1.5
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MailSessionDefinition {

    /**
     * Description of this mail session.
     */
    String description() default "";

    /**
     * JNDI name by which the mail session will be registered.
     */
    String name();

    /**
     * Store protocol name.
     */
    String storeProtocol() default "";

    /**
     * Transport protocol name.
     */
    String transportProtocol() default "";

    /**
     * Host name for the mail server.
     */
    String host() default "";

    /**
     * User name to use for authentication.
     */
    String user() default "";

    /**
     * Password to use for authentication.
     */
    String password() default "";

    /**
     * From address for the user.
     */
    String from() default "";

    /**
     * Properties to include in the Session.
     * Properties are specified using the format:
     * propertyName=propertyValue with one property per array element.
     */
    String[] properties() default {};
}
