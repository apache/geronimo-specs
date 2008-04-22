package javax.xml.bind;

import javax.xml.validation.Schema;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 10:36:09 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Binder<XmlNode> {

    public abstract ValidationEventHandler getEventHandler() throws JAXBException;

    public abstract Object getJAXBNode(XmlNode xmlNode);

    public abstract Object getProperty(String name) throws PropertyException;

    public abstract Schema getSchema();

    public abstract XmlNode getXmlNode(Object jaxbObject);

    public abstract void marshal(Object jaxbObject, XmlNode xmlNode) throws JAXBException;

    public abstract void setEventHandler(ValidationEventHandler handler) throws JAXBException;

    public abstract void setProperty(String name, Object value) throws PropertyException;

    public abstract void setSchema(Schema schema);

    public abstract Object unmarshal(XmlNode xmlNode) throws JAXBException;

    public abstract <T> JAXBElement<T> unmarshal(XmlNode xmlNode, Class<T> declaredType) throws JAXBException;

    public abstract Object updateJAXB(XmlNode xmlNode) throws JAXBException;

    public abstract XmlNode updateXML(Object jaxbObject) throws JAXBException;

    public abstract XmlNode updateXML(Object jaxbObject, XmlNode xmlNode) throws JAXBException; 

    
}
