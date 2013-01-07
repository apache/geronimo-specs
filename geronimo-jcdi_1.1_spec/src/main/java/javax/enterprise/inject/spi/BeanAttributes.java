package javax.enterprise.inject.spi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;


/**
 * Defines the mutable parts of the {@link Bean} interface.
 *
 * @since 1.1‚
 */
public interface BeanAttributes<T> {
    /**
     * Returns api types of a bean.
     *
     * @return api types of a bean
     */
    public abstract Set<Type> getTypes();

    /**
     * Returns qualifiers of a bean.
     *
     * @return qualifiers of a bean
     */
    public abstract Set<Annotation> getQualifiers();

    /**
     * Returns scope of a bean.
     *
     * @return scope
     */
    public abstract Class<? extends Annotation> getScope();

    /**
     * Returns name of a bean.
     *
     * @return name of a bean
     */
    public abstract String getName();

    /**
     * If bean is nullable return true, false
     * otherwise.
     *
     * <p>
     * Nullable means that if producer
     * bean api type is primitive, its nullable property
     * will be false.
     * </p>
     *
     * @return true if bean is nullable.
     */
    public abstract boolean isNullable();


    /**
     * Returns bean stereotypes.
     *
     * @return bean stereotypes
     */
    public Set<Class<? extends Annotation>> getStereotypes();

    /**
     * Returns true if declares as policy
     *
     * @return true if declares as policy
     */
    public boolean isAlternative();
}
