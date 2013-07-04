package javax.enterprise.inject.spi;

/**
 * This event gets fired for AnnotatedTypes which are not a result
 * of the scanning process but got manually added.
 */
public interface ProcessSynthethicAnnotatedType<X> extends ProcessAnnotatedType<X>
{
    /**
     * @return the Extension which added this AnnotatedType
     */
    public Extension getSource();
}
