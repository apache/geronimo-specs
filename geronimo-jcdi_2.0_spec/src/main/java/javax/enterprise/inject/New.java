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
package javax.enterprise.inject;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Qualifier;

@Target( { FIELD, PARAMETER, METHOD, TYPE})
@Retention(RUNTIME)
@Documented
@Qualifier
public @interface New
{
    /**
     * <p>May be used to declare a class which should be used for injection.
     * This defaults to the type which is defined at the injection point.</p>
     *
     * <p>Technically this is a qualifier, but it has a very special handling
     * defined by the specification. It will create a new Contextual Instance
     * of the given class by calling the default constructor. The created
     * Contextual Instance will be treated as being &#064;Dependent to the
     * instance the injection point belongs to.</p>
     *
     * <p>&#064;New also works for creating Contextual Instances of classes which are
     * <i>not</i> part of a bean archive (BDA, aka a jar with a META-INF/beans.xml).
     * Note that from a practical point &#064;New is rarely useful. If you don't have
     * a beans.xml then you will most probably also not have any CDI feature in that class.
     * and if you otoh do have such a BDA, then you can inject the bean directly anyway.
     * The only real usage is to inject a new 'dependent' instance of a CDI bean which
     * has a different scope already defined.
     *
     * <p>
     * Example:
     * <pre>
     * &#064;Inject &#064;New SomeClass instance;
     * </pre>
     * </p>
     *
     * <p><b>Attention:</b> &#064;New only works for InjectionPoints, it is not
     * possible to resolve a new-bean programatically via
     * {@link javax.enterprise.inject.spi.BeanManager#getBeans(java.lang.reflect.Type, java.lang.annotation.Annotation...)}
     * if there was no &#064;New InjectionPoint of that type in the scanned classes.</p>
     *
     * @return the class of the bean which should be injected
     */
    Class<?> value() default New.class;

    final class Literal extends AnnotationLiteral<New> implements New
    {
        private static final long serialVersionUID = 1L;

        private final Class<?> value;


        public static Literal of(Class<?> value)
        {
            return new Literal(value);
        }

        private Literal(Class<?> value)
        {
            this.value = value;
        }

        @Override
        public Class<?> value()
        {
            return value;
        }
    }

}
