package javax.xml.bind.util;

import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.bind.Marshaller;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.xml.sax.XMLReader;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.EntityResolver;
import org.xml.sax.DTDHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.ext.LexicalHandler;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 1:31:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class JAXBSource extends SAXSource {

    public JAXBSource(JAXBContext context, Object contentObject) throws JAXBException {
        if (context == null) {
            throw new JAXBException("context must not be null");
        }
        if (contentObject == null) {
            throw new JAXBException("contentObject must not be null");
        }
        setXMLReader(new Reader(context.createMarshaller(), contentObject));
        setInputSource(new InputSource());
    }

    public JAXBSource(final Marshaller marshaller, final Object contentObject) throws JAXBException {
        if (marshaller == null) {
            throw new JAXBException("marshaller must not be null");
        }
        if (contentObject == null) {
            throw new JAXBException("contentObject must not be null");
        }
        setXMLReader(new Reader(marshaller, contentObject));
        setInputSource(new InputSource());
    }

    private static class Reader implements XMLReader {

        private LexicalHandler lexicalHandler;
        private EntityResolver entityResolver;
        private DTDHandler dtdHandler;
        private XMLFilterImpl repeater;
        private ErrorHandler errorHandler;
        private final Marshaller marshaller;
        private final Object contentObject;

        public Reader(Marshaller marshaller, Object contentObject) {
            this.marshaller = marshaller;
            this.contentObject = contentObject;
            repeater = new XMLFilterImpl();
        }

        public boolean getFeature(String name) throws SAXNotRecognizedException {
            if (name.equals("http://xml.org/sax/features/namespaces")) {
                return true;
            }
            if (name.equals("http://xml.org/sax/features/namespace-prefixes")) {
                return false;
            }
            throw new SAXNotRecognizedException(name);
        }

        public void setFeature(String name, boolean value) throws SAXNotRecognizedException {
            if (name.equals("http://xml.org/sax/features/namespaces") && value) {
                return;
            }
            if (name.equals("http://xml.org/sax/features/namespace-prefixes") && !value) {
                return;
            }
            throw new SAXNotRecognizedException(name);
        }

        public Object getProperty(String name) throws SAXNotRecognizedException {
            if("http://xml.org/sax/properties/lexical-handler".equals(name)) {
                return lexicalHandler;
            }
            throw new SAXNotRecognizedException(name);
        }

        public void setProperty(String name, Object value) throws SAXNotRecognizedException {
            if("http://xml.org/sax/properties/lexical-handler".equals(name)) {
                lexicalHandler = (LexicalHandler) value;
                return;
            }
            throw new SAXNotRecognizedException(name);
        }

        public void setEntityResolver(EntityResolver resolver) {
            entityResolver = resolver;
        }

        public EntityResolver getEntityResolver() {
            return entityResolver;
        }

        public void setDTDHandler(DTDHandler handler) {
            dtdHandler = handler;
        }

        public DTDHandler getDTDHandler() {
            return dtdHandler;
        }

        public void setContentHandler(ContentHandler handler) {
            repeater.setContentHandler(handler);
        }

        public ContentHandler getContentHandler() {
            return repeater.getContentHandler();
        }

        public void setErrorHandler(ErrorHandler handler) {
            errorHandler = handler;
        }

        public ErrorHandler getErrorHandler() {
            return errorHandler;
        }

        public void parse(InputSource input) throws SAXException {
            parse();
        }

        public void parse(String systemId) throws SAXException {
            parse();
        }

        public void parse() throws SAXException {
            try {
                marshaller.marshal(contentObject, repeater);
            } catch(JAXBException e) {
                SAXParseException se = new SAXParseException(e.getMessage(), null, null, -1, -1, e);
                if (errorHandler != null) {
                    errorHandler.fatalError(se);
                }
                throw se;
            }
        }

    }
}
