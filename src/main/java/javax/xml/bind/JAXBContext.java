package javax.xml.bind;

import java.util.Map;
import java.util.Collections;

import org.w3c.dom.Node;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 10:26:26 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class JAXBContext {

    public static final String JAXB_CONTEXT_FACTORY = "javax.xml.bind.context.factory";

    protected JAXBContext() {
    }

    public Binder<Node> createBinder() {
        throw new UnsupportedOperationException();
    }

    public <T> Binder<T> createBinder(Class<T> domType) {
        throw new UnsupportedOperationException();
    }

    public JAXBIntrospector createJAXBIntrospector() {
        throw new UnsupportedOperationException();
    }

    public abstract Marshaller createMarshaller();

    public abstract Unmarshaller createUnmarshaller();

    @Deprecated
    public abstract Validator createValidator();

    public void generateSchema(SchemaOutputResolver resolver) {
        throw new UnsupportedOperationException();
    }

    public static JAXBContext newInstance(Class... classesToBeBound) throws JAXBException {
        return newInstance(classesToBeBound, Collections.<String, Object>emptyMap());
    }

    public static JAXBContext newInstance(Class[] classesToBeBound, Map<String, ?> properties) throws JAXBException {
        for (Class cl : classesToBeBound) {
            if (cl == null) {
                throw new IllegalArgumentException();
            }
        }
        return ContextFinder.find(classesToBeBound, properties);
    }

    public static JAXBContext newInstance(String contextPath) throws JAXBException {
        return newInstance(contextPath, Thread.currentThread().getContextClassLoader());
    }

    public static JAXBContext newInstance(String contextPath, ClassLoader classLoader) throws JAXBException {
        return newInstance(contextPath, classLoader, Collections.<String, Object>emptyMap());
    }

    public static JAXBContext newInstance(String contextPath, ClassLoader classLoader, Map<String, ?> properties) throws JAXBException {
        return ContextFinder.find(contextPath, classLoader, properties);
    }
}
