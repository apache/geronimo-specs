package javax.xml.bind;

import java.io.OutputStream;
import java.io.Writer;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.validation.Schema;
import javax.xml.transform.Result;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamWriter;

import org.w3c.dom.Node;

import org.xml.sax.ContentHandler;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 9:03:51 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Marshaller {

    String JAXB_ENCODING = "jaxb.encoding";
    String JAXB_FORMATTED_OUTPUT = "jaxb.formatted.output";
    String JAXB_FRAGMENT = "jaxb.fragment";
    String JAXB_NO_NAMESPACE_SCHEMA_LOCATION = "jaxb.noNamespaceSchemaLocation";
    String JAXB_SCHEMA_LOCATION = "jaxb.schemaLocation";

    abstract class Listener {
        public void beforeMarshal(Object source) {
        }
        public void afterMarshal(Object source) {
        }
    }

    <A extends XmlAdapter> A getAdapter(Class<A> type);

    AttachmentMarshaller getAttachmentMarshaller();

    ValidationEventHandler getEventHandler() throws JAXBException;

    Listener getListener();

    Node getNode(Object contentTree) throws JAXBException;

    Object getProperty(String name) throws PropertyException;

    Schema getSchema();

    void marshal(Object jaxbElement, ContentHandler handler) throws JAXBException;

    void marshal(Object jaxbElement, Node node) throws JAXBException;

    void marshal(Object jaxbElement, OutputStream os) throws JAXBException;

    void marshal(Object jaxbElement, Result result);

    void marshal(Object jaxbElement, Writer writer) throws JAXBException;

    void marshal(Object jaxbElement, XMLEventWriter writer) throws JAXBException;

    void marshal(Object jaxbElement, XMLStreamWriter writer) throws JAXBException;

    <A extends XmlAdapter> void setAdapter(Class<A> type, A adapter);

    void setAdapter(XmlAdapter adapter);

    void setAttachmentMarshaller(AttachmentMarshaller am);

    void setEventHandler(ValidationEventHandler handler) throws JAXBException;

    void setListener(Listener listener);

    void setProperty(String name, Object value) throws PropertyException;

    void setSchema(Schema schema);

}
