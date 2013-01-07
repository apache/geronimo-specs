package javax.enterprise.inject.spi;

/**
 * <p>Pluggable mechanism to resolve the CDI instance.</p>
 * <p>A container or an application can set this with
 * {@link CDI#setCDIProvider(CDIProvider)}</p>
 *
 * @since 1.1
 */
public interface CDIProvider
{
    public CDI<Object> getCDI();
}
