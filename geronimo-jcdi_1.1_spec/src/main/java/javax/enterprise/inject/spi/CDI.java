package javax.enterprise.inject.spi;

import javax.enterprise.inject.Instance;
import java.util.ServiceLoader;

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
 * @since 1.1
 */
public abstract class CDI<T> implements Instance<T>
{
    private static volatile CDI INSTANCE; // temporary implementation

    public static CDI<Object> current()
    {
        if (INSTANCE == null)
        {
            INSTANCE = ServiceLoader.load(CDIProvider.class).iterator().next().getCDI();
        }
        return INSTANCE; //X TODO implement!
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
        INSTANCE = provider.getCDI();
    }

    public abstract BeanManager getBeanManager();
}
