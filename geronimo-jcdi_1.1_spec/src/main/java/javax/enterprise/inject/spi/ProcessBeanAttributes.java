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

/**
 * Each {@link AnnotatedType} gets scanned by the CDI container and turned
 * into initial {@link BeanAttributes}. Those BeanAttributes can be modified
 * by observing this very ProcessBeanAttributes system event.
 * The modified BeanAttributes will get used to construct the final {@link Bean}.
 *
 */
public interface ProcessBeanAttributes<T>
{
    /**
     * @return the {@link AnnotatedType} for bean classes, {@link AnnotatedMethod} for producer methods and
     *          {@link AnnotatedField} for producer fields.
     */
    public Annotated getAnnotated();

    /**
     * @return the BeanAttributes parsed from the {@link Annotated}
     */
    public BeanAttributes<T> getBeanAttributes();

    /**
     * Use the given BeanAttributes to later create the {@link Bean} from it.
     * @param beanAttributes
     */
    public void setBeanAttributes(BeanAttributes<T> beanAttributes);

    /**
     * Tell the container it should ignore this Bean.
     */
    public void veto();

    /**
     * Adding definition error. Container aborts
     * processing after calling all observers.
     *
     * @param t throwable
     */
    public void addDefinitionError(Throwable t);
}
