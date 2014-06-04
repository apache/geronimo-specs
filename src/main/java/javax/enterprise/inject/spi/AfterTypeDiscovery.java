package javax.enterprise.inject.spi;

import java.util.List;

/**
 * This event will get fired after the container has completed the
 * class scanning process and all AnnotatedTypes got discovered
 * from the classpath.
 */
public interface AfterTypeDiscovery
{
    /**
     * This method can be used to remove and add {@link javax.enterprise.inject.Alternative}s,
     * but also to change the sorting order
     * {@link javax.enterprise.inject.Alternative}s which are only enabled in a certain
     * BeanArchive are not included in this list.
     * @return the ordered, mutable List of enabled {@link javax.enterprise.inject.Alternative}s
     */
    public List<Class<?>> getAlternatives();

    /**
     * This method can be used to remove and add enabled CDI Interceptors,
     * but also to change the sorting order.
     * Interceptors which are only enabled in a certain BeanArchive are not included in this list.
     * @return the ordered, mutable List of Classes which are annotated with {@link Interceptor}
     *      and globally enabled.
     */
    public List<Class<?>> getInterceptors();

    /**
     * This method can be used to remove and add enabled Decorators,
     * but also to change the sorting order.
     * Decorators which are only enabled in a certain BeanArchive are not included in this list.
     * @return the ordered, mutable List of Classes which are annotated with {@link Decorator}
     *      and globally enabled.
     */
    public List<Class<?>> getDecorators();

    /**
     * Allows to a synthetic annotated type.
     * This method shall get used if you like to add a new
     * AnnotatedType for a class which already gets scanned by the container.
     * You should provide an unique id if there is already another AnnotatedType
     * for the same class.
     *
     * The AnnotatedTypes added via this method will not get passed
     * to Extensions via {@link ProcessAnnotatedType} but only via
     * {@link ProcessSyntheticAnnotatedType}
     *
     * @param type
     * @param id the unique id or <code>null</code>
     *
     * @see AfterBeanDiscovery#getAnnotatedType(Class, String)
     * @see AfterBeanDiscovery#getAnnotatedTypes(Class)
     *
     */
    public void addAnnotatedType(AnnotatedType<?> type, String id);
}
