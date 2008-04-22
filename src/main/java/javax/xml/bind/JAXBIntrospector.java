package javax.xml.bind;

import javax.xml.namespace.QName;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 10:43:40 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class JAXBIntrospector {

    public abstract QName getElementName(Object jaxbElement);

    public static Object getValue(Object jaxbElement) {
        if (jaxbElement instanceof JAXBElement) {
            return ((JAXBElement) jaxbElement).getValue();
        } else {
            return jaxbElement;
        }
    }

    public abstract boolean isElement(Object object);
}
