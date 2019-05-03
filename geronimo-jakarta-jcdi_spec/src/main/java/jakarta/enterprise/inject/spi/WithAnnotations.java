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
package jakarta.enterprise.inject.spi;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This allows for applying an annotation filter to
 * {@link jakarta.enterprise.inject.spi.ProcessAnnotatedType}.
 *
 * Sample usage:
 * <pre>
 *     public void processWindowBeans(&#064;Observes &#064;WithAnnotation(WindowBean.class) ProcessAnnotatedType pat) {..}
 * </pre>
 * This Extension method e.g. will get fired for all classes which have a <code>&#064;WindowBean</code> annotation.
 *
 * @since 1.1
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WithAnnotations
{
    /**
     * @return the annotations the {@link jakarta.enterprise.inject.spi.ProcessAnnotatedType} should get filtered for
     */
    Class<? extends Annotation>[] value();
}
