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
package javax.enterprise.inject.spi;

import java.lang.annotation.Annotation;

/**
 * Container fires this event before discovery
 * of the beans process.
 * 
 * @version $Rev$ $Date$
 *
 */
public interface BeforeBeanDiscovery
{
    /**
     * Declares a new qualifier.
     * 
     * @param qualifier qualifier
     */
    public void addQualifier(Class<? extends Annotation> qualifier);

    /**
     * Declare a new qualifier via the information from the given AnnotatedType.
     * @param qualifier
     */
    public void addQualifier(AnnotatedType<? extends Annotation> qualifier);

    
    /**
     * Declares a new scope.
     * 
     * @param scope scope
     * @param normal is normal or not
     * @param passivating passivated or not
     */
    public void addScope(Class<? extends Annotation> scope, boolean normal, boolean passivating);
    
    /**
     * Declares a new stereotype.
     * 
     * @param stereotype stereotype class
     * @param stereotypeDef meta annotations
     */
    public void addStereotype(Class<? extends Annotation> stereotype, Annotation... stereotypeDef);
    
    /**
     * Declares a new interceptor binding.
     * 
     * @param binding binding class
     * @param bindingDef meta annotations
     */
    public void addInterceptorBinding(Class<? extends Annotation> binding, Annotation... bindingDef);

    /**
     * Declare a new interceptor binding via the information from the given AnnotatedType.
     * @param bindingType
     */
    public void addInterceptorBinding(AnnotatedType<? extends Annotation> bindingType);
    
    /**
     * Adds new annotated type.
     * This version shall be used when adding AnnotatedTypes for classes which are
     * not yet scanned by the CDI container.
     * 
     * @param type annotated type
     */
    public void addAnnotatedType(AnnotatedType<?> type);

    /**
     * Adds new annotated type for classes which are not picked up by the CDI container
     * or if you like to add multiple AnnotatedType for the same class.
     *
     * @param type annotated type
     * @param id to distinguish AnnotatedTypes for the same class.
     */
     public void addAnnotatedType(AnnotatedType<?> type, String id);
}
