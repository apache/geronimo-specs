/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package javax.xml.bind.helpers;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.net.MalformedURLException;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.UnmarshallerHandler;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;
import javax.xml.bind.attachment.AttachmentUnmarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.validation.Schema;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.dom.DOMSource;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Node;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public abstract class AbstractUnmarshallerImpl implements Unmarshaller {

    protected boolean validating;
    private ValidationEventHandler eventHandler;
    private XMLReader reader;

    protected UnmarshalException createUnmarshalException(SAXException e) {
        Exception nested = e.getException();
        if (nested instanceof UnmarshalException) {
            return (UnmarshalException)nested;
        } else if(nested instanceof RuntimeException) {
            throw (RuntimeException)nested;
        } else if (nested != null) {
            return new UnmarshalException(nested);
        } else {
            return new UnmarshalException(e);
        }
    }

    protected XMLReader getXMLReader() throws JAXBException {
        if (reader == null) {
            try {
                SAXParserFactory parserFactory = SAXParserFactory.newInstance();
                parserFactory.setNamespaceAware(true);
                parserFactory.setValidating(false);
                reader = parserFactory.newSAXParser().getXMLReader();
            } catch(ParserConfigurationException e) {
                throw new JAXBException(e);
            } catch(SAXException e) {
                throw new JAXBException(e);
            }
        }
        return reader;
    }

    public <A extends XmlAdapter> A getAdapter(Class<A> type) {
        throw new UnsupportedOperationException();
    }

    public AttachmentUnmarshaller getAttachmentUnmarshaller() {
        throw new UnsupportedOperationException();
    }

    public ValidationEventHandler getEventHandler() throws JAXBException {
        return eventHandler;
    }

    public Listener getListener() {
        throw new UnsupportedOperationException();
    }

    public Object getProperty(String name) throws PropertyException {
        if(name == null) {
            throw new IllegalArgumentException("name must not be null");
        }
        throw new PropertyException(name);
    }

    public Schema getSchema() {
        throw new UnsupportedOperationException();
    }

    public boolean isValidating() throws JAXBException {
        return validating;
    }

    public <A extends XmlAdapter> void setAdapter(Class<A> type, A adapter) {
        throw new UnsupportedOperationException();
    }

    public void setAdapter(XmlAdapter adapter) {
        if (adapter == null) {
            throw new IllegalArgumentException();
        }
        setAdapter((Class<XmlAdapter>) adapter.getClass(), adapter);
    }

    public void setAttachmentUnmarshaller(AttachmentUnmarshaller au) {
        throw new UnsupportedOperationException();
    }

    public void setEventHandler(ValidationEventHandler handler) throws JAXBException {
        if (handler == null) {
            handler = new DefaultValidationEventHandler();
        }
        eventHandler = handler;
    }

    public void setListener(Listener listener) {
        throw new UnsupportedOperationException();
    }

    public void setProperty(String name, Object value) throws PropertyException {
        if(name == null) {
            throw new IllegalArgumentException("name must not be null");
        }
        throw new PropertyException(name, value);
    }

    public void setSchema(Schema schema) {
        throw new UnsupportedOperationException();
    }

    public void setValidating(boolean validating) throws JAXBException {
        this.validating = validating;
    }

    public final Object unmarshal(File file) throws JAXBException {
        if (file == null) {
            throw new IllegalArgumentException("file must not be null");
        }
        try
        {
            String path = file.getAbsolutePath();
            if (File.separatorChar != '/') {
                path = path.replace(File.separatorChar, '/');
            }
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (!path.endsWith("/") && file.isDirectory()) {
                path = path + "/";
            }
            return unmarshal(new URL("file", "", path));
        }
        catch(MalformedURLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public final Object unmarshal(InputSource source) throws JAXBException {
        if (source == null) {
            throw new IllegalArgumentException("source must not be null");
        }
        return unmarshal(getXMLReader(), source);
    }

    public final Object unmarshal(InputStream is) throws JAXBException {
        if (is == null) {
            throw new IllegalArgumentException("is must not be null");
        }
        return unmarshal(new InputSource(is));
    }

    public <T> JAXBElement<T> unmarshal(Node node, Class<T> declaredType) throws JAXBException {
        throw new UnsupportedOperationException();
    }

    public final Object unmarshal(Reader reader) throws JAXBException {
        if (reader == null) {
            throw new IllegalArgumentException("reader must not be null");
        }
        return unmarshal(new InputSource(reader));
    }

    public Object unmarshal(Source source) throws JAXBException {
        if (source == null) {
            throw new IllegalArgumentException("source must not be null");
        } else if (source instanceof SAXSource) {
            SAXSource saxSource = (SAXSource) source;
            XMLReader reader = saxSource.getXMLReader();
            if (reader == null) {
                reader = getXMLReader();
            }
            return unmarshal(reader, saxSource.getInputSource());
        } else if (source instanceof StreamSource) {
            StreamSource ss = (StreamSource) source;
            InputSource is = new InputSource();
            is.setSystemId(ss.getSystemId());
            is.setByteStream(ss.getInputStream());
            is.setCharacterStream(ss.getReader());
            return unmarshal(is);
        } else if (source instanceof DOMSource)
            return unmarshal(((DOMSource) source).getNode());
        else
            throw new IllegalArgumentException();
    }

    protected abstract Object unmarshal(XMLReader xmlreader, InputSource inputsource) throws JAXBException;

    public <T> JAXBElement<T> unmarshal(Source source, Class<T> declaredType) throws JAXBException {
        throw new UnsupportedOperationException();
    }

    public final Object unmarshal(URL url) throws JAXBException {
        if(url == null) {
            throw new IllegalArgumentException("url must not be null");
        }
        return unmarshal(new InputSource(url.toExternalForm()));
    }

    public Object unmarshal(XMLEventReader reader) throws JAXBException {
        throw new UnsupportedOperationException();
    }

    public <T> JAXBElement<T> unmarshal(XMLEventReader reader, Class<T> declaredType) throws JAXBException {
        throw new UnsupportedOperationException();
    }

    public Object unmarshal(XMLStreamReader reader) throws JAXBException {
        throw new UnsupportedOperationException();
    }

    public <T> JAXBElement<T> unmarshal(XMLStreamReader reader, Class<T> declaredType) throws JAXBException {
        throw new UnsupportedOperationException();
    }
}
