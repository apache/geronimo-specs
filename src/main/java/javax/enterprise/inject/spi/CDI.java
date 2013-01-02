package javax.enterprise.inject.spi;

import javax.enterprise.inject.Instance;

/**
 * <p>Static helper class to access the {@link BeanManager}</p>
 *
 * TODO not yet implemented!
 *
 * <p>Usage:
 * <pre>
 * BeanManager bm = CDI.current().getBeanManager();
 * </pre>
 * </p>
 *
 *
 */
public abstract class CDI<T> implements Instance<T>
{

    public static CDI<Object> current()
    {
        return null; //X TODO implement!
    }

    /**
     * <p>A container or an application can set this manually. If not
     * we will use the {@link java.util.ServiceLoader} and use the
     * first service we find.</p>
     *
     * TODO: clarify if this is per 'application' or general?
     *
     * @param provider to use
     */
    public static void setCDIProvider(CDIProvider provider)
    {
        //X TODO implement!
    }

    public abstract BeanManager getBeanManager();
}
