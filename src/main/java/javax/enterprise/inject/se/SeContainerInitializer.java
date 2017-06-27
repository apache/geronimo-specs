/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package javax.enterprise.inject.se;


import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.ServiceLoader;

import javax.enterprise.inject.spi.Extension;

/**
 * This is a builder class for a synthetic bean archive that will be used in an SE Deployment of CDI.
 *
 * Invoking newInstance() creates a new one from a ServiceLoader, and by default will load all JARs from the classpath
 * unless you invoke disableDiscovery() and manually load classes in to the archive.
 */
@SuppressWarnings("unchecked")
public abstract class SeContainerInitializer
{

    /**
     * Instantiates a new SeContainerInitializer via ServiceLoader
     * @return the SeContainerInitializer implementation found via ServiceLoader
     * @throws IllegalStateException if 0 or more than 1 SeContainerInitializer found.
     */
    public static SeContainerInitializer newInstance()
    {
        ServiceLoader<SeContainerInitializer> serviceLoader =
                ServiceLoader.load(SeContainerInitializer.class, SeContainerInitializer.class.getClassLoader());
        long exactSize = serviceLoader.spliterator().getExactSizeIfKnown();
        if(exactSize == 0)
        {
            throw new IllegalStateException("No valid implementation of SeContainerInitializer found via ServiceLoader");
        }
        else if(exactSize > 1)
        {
            throw new IllegalStateException("Multiple implementations ("+exactSize+") of SeContainerInitializer found via ServiceLoader");
        }
        return serviceLoader.iterator().next();
    }

    /**
     * Adds the listed classes to the resulting synthetic bean archive
     * @param classes
     * @return this
     */
    public abstract SeContainerInitializer addBeanClasses(Class<?>... classes);

    /**
     * Adds packages of the given classes to the synthetic bean archive, the result of packageClass.getPackage()
     *
     * @param packageClasses
     * @return this
     */
    public abstract SeContainerInitializer addPackages(Class<?>... packageClasses);

    /**
     * Add packages for the given classes to the synthetic bean archive.
     *
     * @param scanRecursively if true, will recursively check the child packages of each of these classes' packages
     * @param packageClasses
     * @return this
     */
    public abstract SeContainerInitializer addPackages(boolean scanRecursively, Class<?>... packageClasses);

    /**
     * Adds the given packages to the synthetic bean archive.
     *
     * @param packages
     * @return this
     */
    public abstract SeContainerInitializer addPackages(Package... packages);

    /**
     * Adds the given packages to the synthetic bean archive.
     *
     * @param scanRecursively if true, will recursively check the child packages of each of these classes' packages
     * @param packages
     * @return this
     */
    public abstract SeContainerInitializer addPackages(boolean scanRecursively, Package... packages);

    /**
     * Adds the given Extension instances to the synthetic bean archive
     *
     * @param extensions
     * @return this
     */
    public abstract SeContainerInitializer addExtensions(Extension... extensions);

    /**
     * Adds the given Extension classes to the synthetic bean archive
     *
     * @param extensions
     * @return this
     */
    public abstract SeContainerInitializer addExtensions(Class<? extends Extension>... extensions);

    /**
     * Enables the given interceptors in the synthetic bean archive
     *
     * @param interceptorClasses
     * @return this
     */
    public abstract SeContainerInitializer enableInterceptors(Class<?>... interceptorClasses);

    /**
     * Enables the given decorators in the synthetic bean archive
     * @param decoratorClasses
     * @return this
     */
    public abstract SeContainerInitializer enableDecorators(Class<?>... decoratorClasses);

    /**
     * Adds the given alternatives to the list of available alternatives in the bean archive
     *
     * @param alternativeClasses
     * @return this
     */
    public abstract SeContainerInitializer selectAlternatives(Class<?>... alternativeClasses);

    /**
     * Adds the given alternative stereotypes to the list of available alternative stereotypes in the bean archive
     *
     * @param alternativeStereotypeClasses
     * @return this
     */
    public abstract SeContainerInitializer selectAlternativeStereotypes(Class<? extends Annotation>... alternativeStereotypeClasses);

    /**
     * Adds a configuration property to the container
     *
     * @param key
     * @param value
     * @return this
     */
    public abstract SeContainerInitializer addProperty(String key, Object value);

    /**
     * Overwrites all existing properties with the contents of the new given map
     *
     * @param properties
     * @return this
     */
    public abstract SeContainerInitializer setProperties(Map<String, Object> properties);

    /**
     * Disables bean discovery of the classpath and instead relies solely on the classes defined in the archive
     *
     * @return this
     */
    public abstract SeContainerInitializer disableDiscovery();

    /**
     * Sets the default ClassLoader for this synthetic bean archive
     *
     * @param classLoader
     * @return this
     */
    public abstract SeContainerInitializer setClassLoader(ClassLoader classLoader);

    /**
     * Bootstraps the container that has been built from this SeContainerInitializer
     *
     * @return a new SeContainer representing this synthetic bean archive
     */
    public abstract SeContainer initialize();

}