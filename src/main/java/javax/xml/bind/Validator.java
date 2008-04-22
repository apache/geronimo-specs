package javax.xml.bind;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 10:05:54 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Validator {

    ValidationEventHandler getEventHandler() throws JAXBException;

    Object getProperty(String name) throws PropertyException;

    void setEventHandler(ValidationEventHandler handler) throws JAXBException;

    void setProperty(String name, Object value) throws PropertyException;

    boolean validate(Object subRoot) throws JAXBException;

    boolean validateRoot(Object rootObj) throws JAXBException;
}
