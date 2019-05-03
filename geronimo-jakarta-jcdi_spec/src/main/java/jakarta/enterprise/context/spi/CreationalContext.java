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
package jakarta.enterprise.context.spi;

/**
 * <p>The <code>CreationalContext</code> holds incomplete Bean instances and
 * references to all{@link jakarta.enterprise.context.Dependent} scoped
 * contextual instances injected into a {@link jakarta.enterprise.context.NormalScope}d
 * bean.</p>
 *
 * <p>E.g. consider we create a Contextual Instance of a
 * {@link jakarta.enterprise.context.SessionScoped} <code>UserInformation</code>
 * bean which has a dependent injection point
 * (e.g. a field <code>private &#064;Inject Helper helper;</code> )
 * In that case the CreationalContext of the <code>UserInformation</code> instance
 * will contain the information about the <code>helper</code> instance.
 * This is needed to properly destroy the <code>helper</code> once the
 * <code>UserInformation</code> gets destroyed. In our example this will
 * happen at the end of the Session.
 * </p>
 *
 * <p><b>Attention:</b> If you create a {@link jakarta.enterprise.context.Dependent} instance
 * manually using
 * {@link jakarta.enterprise.inject.spi.BeanManager#getReference(jakarta.enterprise.inject.spi.Bean, java.lang.reflect.Type, CreationalContext)}
 * then you must store away the CreationalContext. You will need it for properly destroying the created instance
 * via {@link jakarta.enterprise.inject.spi.Bean#destroy(Object, CreationalContext)}.
 * This is <i>not</i> needed for {@link jakarta.enterprise.context.NormalScope}d beans as they
 * maintain their lifecycle themself!</p>
 * 
 * @version $Rev$ $Date$
 */
public interface CreationalContext<T>
{
    /**
     * Puts new incomplete instance into the creational context.
     * 
     * @param incompleteInstance incomplete webbeans instance
     */
    void push(T incompleteInstance);
    
    /**
     * Destorys all dependent objects of the instance
     * that is being destroyed.
     */
    void release();

}
