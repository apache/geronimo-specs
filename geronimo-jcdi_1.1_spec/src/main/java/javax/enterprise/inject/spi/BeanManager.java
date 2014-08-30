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
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Set;

import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.AmbiguousResolutionException;
import javax.enterprise.inject.InjectionException;
import javax.enterprise.inject.UnsatisfiedResolutionException;


/**
 * <p>The interface <code>BeanManager</code> is the central point for dealing with CDI.
 * The BeanManager provides operations for resolving CDI {@link Bean}s,
 * obtaining the Contextual References of them, etc. There are operations
 * related with;</p>
 * 
 * <ul>
 *  <li>Firing observer events</li>
 *  <li>Creating {@link CreationalContext}s</li>
 *  <li>Resolution of beans, interceptors, decorators and observers</li>
 *  <li>Other utility methods etc..</li>
 * </ul>
 *
 */
public interface BeanManager
{
    /**
     * Returns a bean instance reference for the given bean.
     * 
     * @param bean bean that its reference is getting
     * @param beanType bean api type that is implemented by the proxy
     * @param ctx creational context is used to destroy any object with scope <code>@Dependent</code>
     * @return bean reference
     * @throws IllegalArgumentException if given bean type is not api type of the given bean object
     * @throws java.lang.IllegalStateException if this method gets called before the AfterDeploymentValidation event is fired.
     */
    public Object getReference(Bean<?> bean, Type beanType, CreationalContext<?> ctx);
    
    /**
     * Gets injection point bean reference.
     * 
     * @param injectionPoint injection point definition
     * @param ctx creational context that is passed to the {@link Bean#create(CreationalContext)} method
     * @return bean reference
     * @throws UnsatisfiedResolutionException if no bean found for the given injection point
     * @throws AmbiguousResolutionException if more than one bean found
     * @throws java.lang.IllegalStateException if this method gets called before the AfterDeploymentValidation event is fired.
     */
    public Object getInjectableReference(InjectionPoint injectionPoint, CreationalContext<?> ctx);
    
    /**
     * Returns a new creational context implementation.
     * 
     * @return new creational context
     */
    public <T> CreationalContext<T> createCreationalContext(Contextual<T> contextual);
    
    /**
     * Returns set of beans that their api types contain
     * given bean type and given qualifiers.
     * 
     * <p>
     * If no qualifier is given, <code>@Current</code> is assumed.
     * </p>
     * 
     * @param beanType required bean type
     * @param qualifiers required qualifiers
     * @return set of beans
     * @throws IllegalArgumentException given bean type is a {@link TypeVariable}
     * @throws IllegalArgumentException given qualifier annotation is not a qualifier
     * @throws IllegalArgumentException same qualifier is given
     * @throws java.lang.IllegalStateException if this method gets called before the AfterBeanDiscovery event is fired.
     */
    public Set<Bean<?>> getBeans(Type beanType, Annotation... qualifiers);
        
    /**
     * Returns set of beans with given Expression Language name.
     * 
     * @param name name of the bean
     * @return set of beans with given name
     * @throws java.lang.IllegalStateException if this method gets called before the AfterBeanDiscovery event is fired.
     */
    public Set<Bean<?>> getBeans(String name);    
        
    /**
     * Returns passivation capable bean given id.
     * 
     * @param id bean id
     * @return passivation capable bean given id
     * @throws java.lang.IllegalStateException if this method gets called before the AfterBeanDiscovery event is fired.
     */
    public Bean<?> getPassivationCapableBean(String id);
    
    /**
     * Returns a bean object that is resolved according to the type safe resolution rules.
     * 
     * @param <X> bean class info
     * @param beans set of beans
     * @return bean that is resolved according to the type safe resolution rules
     * @throws AmbiguousResolutionException if ambigious exists
     * @throws java.lang.IllegalStateException if this method gets called before the AfterBeanDiscovery event is fired.
     */
    public <X> Bean<? extends X> resolve(Set<Bean<? extends X>> beans);
        
    /**
     * Fires an event with given even object and qualifiers.
     * 
     * @param event observer event object
     * @param qualifiers event qualifiers
     * @throws IllegalArgumentException event object contains a {@link TypeVariable}
     * @throws IllegalArgumentException given qualifier annotation is not a qualifier
     * @throws IllegalArgumentException same qualifier is given
     */
    public void fireEvent(Object event, Annotation... qualifiers);
        
    /**
     * Returns set of observer methods.
     * 
     * @param <T> event type
     * @param event even object
     * @param qualifiers event qualifiers
     * @return set of observer methods
     * @throws java.lang.IllegalStateException if this method gets called before the AfterBeanDiscovery event is fired.
     */
    public <T> Set<ObserverMethod<? super T>> resolveObserverMethods(T event, Annotation... qualifiers);
    
    /**
     * Returns a list of decorator.
     * 
     * @param types bean types of the decorated bean
     * @param qualifiers decorated bean qualifiers
     * @return list of decorator
     * @throws IllegalArgumentException given qualifier annotation is not a qualifier
     * @throws IllegalArgumentException same qualifier is given
     * @throws IllegalArgumentException if types is empty set
     * @throws java.lang.IllegalStateException if this method gets called before the AfterBeanDiscovery event is fired.
     */
    List<Decorator<?>> resolveDecorators(Set<Type> types, Annotation... qualifiers);
    
    /**
     * Returns a list of interceptor.
     * 
     * @param type interception type
     * @param interceptorBindings interceptor bindings
     * @return list of interceptor
     * @throws IllegalArgumentException given binding annotation is not a binding
     * @throws IllegalArgumentException same binding is given
     * @throws IllegalArgumentException binding is not an interceptor binding
     * @throws java.lang.IllegalStateException if this method gets called before the AfterBeanDiscovery event is fired.
     */
    List<Interceptor<?>> resolveInterceptors(InterceptionType type, Annotation... interceptorBindings);
    
    /**
     * Validates injection point.
     * 
     * @param injectionPoint injection point
     * @throws InjectionException if problem exist
     * @throws java.lang.IllegalStateException if this method gets called before the AfterBeanDiscovery event is fired.
     */
    public void validate(InjectionPoint injectionPoint);
    
    /**
     * Returns true if given type is a scope type, false otherwise.
     * 
     * @param annotationType annotation type
     * @return true if given type is a scope type, false otherwise
     */
    public boolean isScope(Class<? extends Annotation> annotationType);
    
    /**
     * Returns true if given type is a normal scope type, false otherwise.
     * 
     * @param annotationType annotation type
     * @return true if given type is a scope type, false otherwise
     */
    public boolean isNormalScope(Class<? extends Annotation> annotationType);
    
    /**
     * Returns true if given type is a passivating scope type, false otherwise.
     * 
     * @param annotationType annotation type
     * @return true if given type is a scope type, false otherwise
     */
    public boolean isPassivatingScope(Class<? extends Annotation> annotationType);    
    

    /**
     * Returns true if given type is a qualifier, false otherwise.
     * 
     * @param annotationType annotation type
     * @return true if given type is a qualifier, false otherwise
     */    
    public boolean isQualifier(Class<? extends Annotation> annotationType);

    /**
     * Check whether the 2 given qualifiers are the same.
     * This takes {@link javax.enterprise.util.Nonbinding} into account by ignoring
     * those properties.
     * @param qualifier1
     * @param qualifier2
     * @return <code>true</code> if all non-nonbinding attributes are equals, <code>false</code> otherwise
     */
    public boolean areQualifiersEquivalent(Annotation qualifier1, Annotation qualifier2);

    /**
     * @param qualifier
     * @return the hashCode of the Annotation. All {@link javax.enterprise.util.Nonbinding} fields get ignored
     */
    public int getQualifierHashCode(Annotation qualifier);

    /**
     * Returns true if given type is a interceptor binding, false otherwise.
     * 
     * @param annotationType annotation type
     * @return true if given type is a interceptor binding, false otherwise
     */        
    public boolean isInterceptorBinding(Class<? extends Annotation> annotationType);

    /**
     * Check whether the 2 given Interceptor Binding annotations are the same.
     * This takes {@link javax.enterprise.util.Nonbinding} into account by ignoring
     * those properties.
     * @param interceptorBinding1
     * @param interceptorBinding2
     * @return <code>true</code> if all non-nonbinding attributes are equals, <code>false</code> otherwise
     */
    public boolean areInterceptorBindingsEquivalent(Annotation interceptorBinding1, Annotation interceptorBinding2);

    /**
     * @param interceptorBinding
     * @return the hashCode of the Annotation. All {@link javax.enterprise.util.Nonbinding} fields get ignored
     */
    public int getInterceptorBindingHashCode(Annotation interceptorBinding);



    /**
     * Returns true if given type is a stereotype type, false otherwise.
     * 
     * @param annotationType annotation type
     * @return true if given type is a stereotype, false otherwise
     */
    public boolean isStereotype(Class<? extends Annotation> annotationType);
        
    /**
     * Returns a set of meta-annotations that are defined on the binding
     * 
     * @param qualifier binding class
     * @return a set of meta-annotations that are defined on the binding
     */
    public Set<Annotation> getInterceptorBindingDefinition(Class<? extends Annotation> qualifier);
    
    /**
     * Returns a set of meta-annotations that are defined on the stereotype type.
     * 
     * @param stereotype stereotype type class
     * @return a set of meta-annotations that are defined on the stereotype type
     */
    public Set<Annotation> getStereotypeDefinition(Class<? extends Annotation> stereotype);

    /**
     * Returns a context with given scope type.
     * 
     * @param scope scope type class type
     * @return a context with given scope type
     */
    public Context getContext(Class<? extends Annotation> scope);
    
    /**
     * Returns CDI container Expression Language resolver.
     * 
     * @return el resolver
     */
    public ELResolver getELResolver();
    
    /**
     * Returns a {@link AnnotatedType} instance for the given
     * class.
     * 
     * @param <T> class type
     * @param type class
     * @return a {@link AnnotatedType} instance
     */
    public <T> AnnotatedType<T> createAnnotatedType(Class<T> type);
    
    /**
     * Creates a new instance of injection target Bean.
     * 
     * @param <T> bean type
     * @param type annotated type
     * @return injection target
     */
    public <T> InjectionTarget<T> createInjectionTarget(AnnotatedType<T> type);

    /**
     * Create an {@link InjectionPoint} for an annotated field.
     * @param field
     * @throws IllegalArgumentException if there is a definition error on the given field
     * @return injection point
     */
    public InjectionPoint createInjectionPoint(AnnotatedField<?> field);

    /**
     * Create an {@link InjectionPoint} for an annotated parameter.
     * @param parameter
     * @throws IllegalArgumentException if there is a definition error on the given parameter
     * @return injection point
     */
    public InjectionPoint createInjectionPoint(AnnotatedParameter<?> parameter);

    /**
     * @param type
     * @param <T>
     * @return the InjectionTargetFactory which is able to create {@link InjectionTarget}s for the given AnnotatedType.
     */
    public <T> InjectionTargetFactory<T> getInjectionTargetFactory(AnnotatedType<T> type);

    /**
     * @param field
     * @param declaringBean
     * @param <X>
     * @return the ProducerFactory which is able to create {@link Producer}s for the given AnnotatedField.
     */
    public <X> ProducerFactory<X> getProducerFactory(AnnotatedField<? super X> field, Bean<X> declaringBean);

    /**
     * @param method
     * @param declaringBean
     * @param <X>
     * @return the ProducerFactory which is able to create {@link Producer}s for the given AnnotatedMethod.
     */
    public <X> ProducerFactory<X> getProducerFactory(AnnotatedMethod<? super X> method, Bean<X> declaringBean);

    /**
     * This method creates bean meta information from a given {@link AnnotatedType}.
     * The created BeanAttributes can later be used to create a {@link Bean}
     * via {@link #createBean(BeanAttributes, Class, InjectionTargetFactory)} or
     * {@link #createBean(BeanAttributes, Class, ProducerFactory)}
     *
     * @param type
     * @param <T>
     * @return the BeanAttributes created from parsing the given AnnotatedType
     */
    public <T> BeanAttributes<T> createBeanAttributes(AnnotatedType<T> type);

    /**
     * This method creates bean meta information from a given {@link AnnotatedMember}.
     * The created BeanAttributes can later be used to create a {@link Bean}
     * via {@link #createBean(BeanAttributes, Class, InjectionTargetFactory)} or
     * {@link #createBean(BeanAttributes, Class, ProducerFactory)}.
     *
     * @param member
     * @return the BeanAttributes created from parsing the given AnnotatedType
     */
    public BeanAttributes<?> createBeanAttributes(AnnotatedMember<?> member);

    /**
     * Create a {@link Bean} from the given bean attributes.
     * This version of the method uses a given {@link InjectionTargetFactory}.
     * @param attributes
     * @param beanClass
     * @param injectionTargetFactory
     * @param <T>
     * @return the container created Bean
     */
    public <T> Bean<T> createBean(BeanAttributes<T> attributes, Class<T> beanClass,
                                  InjectionTargetFactory<T> injectionTargetFactory);

    /**
     * Create a {@link Bean} from the given bean attributes.
     * This version of the method uses a given {@link ProducerFactory}.
     * @param attributes
     * @param beanClass
     * @param producerFactory
     * @param <T>
     * @return the container created Bean
     */
    public <T, X> Bean<T> createBean(BeanAttributes<T> attributes, Class<X> beanClass,
                                     ProducerFactory<X> producerFactory);

    /**
     * Resolves the Extension instance which gets used by this very BeanManager.
     * The given <code>extensionClass</code> must be the effective class registered
     * into META-INF/services and not some base class.
     *
     * @param extensionClass
     * @param <T>
     * @return the Extension instance of this very BeanManager
     */
    public <T extends Extension> T getExtension(Class<T> extensionClass);

    /**
     * Wrapped around given expression factory and add CDI functionality.
     * @param expressionFactory expression factory
     * @return wrapped expression factory
     */
    public ExpressionFactory wrapExpressionFactory(javax.el.ExpressionFactory expressionFactory);
}
