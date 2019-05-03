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
import java.lang.reflect.Executable;
import java.lang.reflect.Member;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Defines member parameter contract.
 * 
 * @version $Rev$ $Date$
 *
 * @param <X> declaring type
 */
public interface AnnotatedParameter<X> extends Annotated 
{
    /**
     * Returns parameter position.
     *
     * @return parameter position
     */
    int getPosition();

    /**
     * Returns declaring callable member.
     *
     * @return declaring callable member
     */
    AnnotatedCallable<X> getDeclaringCallable();

    default Parameter getJavaParameter()
    {
        Member javaMember = getDeclaringCallable().getJavaMember();
        if (!Executable.class.isInstance(javaMember))
        {
            throw new IllegalStateException("Parameter does not belong to a Constructor or Method: " + javaMember);
        }
        return ((Executable) javaMember).getParameters()[getPosition()];
    }

    @Override
    default <T extends Annotation> Set<T> getAnnotations(Class<T> annotationType)
    {
        return new LinkedHashSet<>(Arrays.asList(getJavaParameter().getAnnotationsByType(annotationType)));
    }

}
