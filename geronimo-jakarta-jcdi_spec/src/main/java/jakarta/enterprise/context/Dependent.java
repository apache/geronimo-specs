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
package jakarta.enterprise.context;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Scope;

/**
 * <p>An @#064;Dependent scoped Contextual instance shares it's lifecycle with
 * the Contextual Instance it got injected to.
 * &#064;Dependent scoped Contextual Instances also do <strong>not</strong> get
 * a normalscoping-proxy (Contextual Reference). They only get a proxy
 * if they are either intercepted or decorated.</p>
 *
 * <p>As of CDI-1.0 this is the default scope for any class if no other
 * scope is explicitly annotated. Since CDI-1.1 this is only the case if the
 * beans.xml has a {@code bean-discovery-mode="all"}</p>
 *
 * <p>
 * Every CDI instance has an associated dependent context. Each dependent context
 * is destroyed with its parent webbeans component instance.
 * </p>
 * 
 * <p>
 * Please see <b>8.3 Dependent pseudo-scope</b> of the specification
 * for further information.
 * </p>
 * 
 */
@Scope
@Target( { ElementType.METHOD, ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Dependent
{
    final class Literal extends AnnotationLiteral<Dependent> implements Dependent
    {
        public static final Literal INSTANCE = new Literal();

        private static final long serialVersionUID = 1L;
    }
}
