package javax.xml.bind;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.attachment.AttachmentUnmarshaller;
import javax.xml.validation.Schema;
import javax.xml.transform.Source;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;

import org.w3c.dom.Node;

import org.xml.sax.InputSource;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 9:26:48 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Unmarshaller {

    abstract class Listener {
        public void beforeUnmarshal(Object target, Object parent) {
        }
        public void afterUnmarshal(Object target, Object parent) {
        }
    }

    <A extends XmlAdapter> A getAdapter(Class<A> type);

    AttachmentUnmarshaller getAttachmentUnmarshaller();

    ValidationEventHandler getEventHandler();

    Listener getListener();

    Object getProperty(String name) throws PropertyException;

    Schema getSchema();

    UnmarshallerHandler getUnmarshallerHandler();

    boolean isValidating() throws JAXBException;

    <A extends XmlAdapter> void setAdapter(Class<A> type, A adapter);

    void setAdapter(XmlAdapter adapter);

    void setAttachmentUnmarshaller(AttachmentUnmarshaller au);

    void setEventHandler(ValidationEventHandler handler) throws JAXBException;

    void setListener(Listener listener);

    void setProperty(String name, Object value) throws PropertyException;

    void setSchema(Schema schema);

    void setValidating(boolean validating);

    Object unmarshal(File f) throws JAXBException;

    Object unmarshal(InputSource source) throws JAXBException;

    Object unmarshal(InputStream is) throws JAXBException;

    Object unmarshal(Node node) throws JAXBException;

    <T> JAXBElement<T> unmarshal(Node node, Class<T> declaredType) throws JAXBException;

    Object unmarshal(Reader reader) throws JAXBException;

    Object unmarshal(Source source) throws JAXBException;

    <T> JAXBElement<T> unmarshal(Source source, Class<T> declaredType) throws JAXBException;

    Object unmarshal(URL url) throws JAXBException;

    Object unmarshal(XMLEventReader reader) throws JAXBException;

    <T> JAXBElement<T> unmarshal(XMLEventReader reader, Class<T> declaredType) throws JAXBException;

    Object unmarshal(XMLStreamReader reader) throws JAXBException;

    <T> JAXBElement<T> unmarshal(XMLStreamReader reader, Class<T> declaredType) throws JAXBException;

}
