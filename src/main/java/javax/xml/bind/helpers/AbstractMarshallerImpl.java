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

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Result;
import javax.xml.validation.Schema;

import org.w3c.dom.Node;

import org.xml.sax.ContentHandler;

public abstract class AbstractMarshallerImpl implements Marshaller {

    static String aliases[] = {
            "UTF-8", "UTF8",
            "UTF-16", "Unicode",
            "UTF-16BE", "UnicodeBigUnmarked",
            "UTF-16LE", "UnicodeLittleUnmarked",
            "US-ASCII", "ASCII",
            "TIS-620", "TIS620",
            "ISO-10646-UCS-2", "Unicode",
            "EBCDIC-CP-US", "cp037",
            "EBCDIC-CP-CA", "cp037",
            "EBCDIC-CP-NL", "cp037",
            "EBCDIC-CP-WT", "cp037",
            "EBCDIC-CP-DK", "cp277",
            "EBCDIC-CP-NO", "cp277",
            "EBCDIC-CP-FI", "cp278",
            "EBCDIC-CP-SE", "cp278",
            "EBCDIC-CP-IT", "cp280",
            "EBCDIC-CP-ES", "cp284",
            "EBCDIC-CP-GB", "cp285",
            "EBCDIC-CP-FR", "cp297",
            "EBCDIC-CP-AR1", "cp420",
            "EBCDIC-CP-HE", "cp424",
            "EBCDIC-CP-BE", "cp500",
            "EBCDIC-CP-CH", "cp500",
            "EBCDIC-CP-ROECE", "cp870",
            "EBCDIC-CP-YU", "cp870",
            "EBCDIC-CP-IS", "cp871",
            "EBCDIC-CP-AR2", "cp918"
    };

    private ValidationEventHandler eventHandler = new DefaultValidationEventHandler();
    private String encoding = "UTF-8";
    private String schemaLocation;
    private String noNSSchemaLocation;
    private boolean formattedOutput;
    private boolean fragment;

    public final void marshal(Object obj, OutputStream os) throws JAXBException {
        checkNotNull(obj, "obj", os, "os");
        marshal(obj, new StreamResult(os));
    }

    public final void marshal(Object obj, Writer w) throws JAXBException {
        checkNotNull(obj, "obj", w, "writer");
        marshal(obj, new StreamResult(w));
    }

    public final void marshal(Object obj, ContentHandler handler) throws JAXBException {
        checkNotNull(obj, "obj", handler, "handler");
        marshal(obj, new SAXResult(handler));
    }

    public final void marshal(Object obj, Node node) throws JAXBException {
        checkNotNull(obj, "obj", node, "node");
        marshal(obj, new DOMResult(node));
    }

    public Node getNode(Object obj) throws JAXBException {
        checkNotNull(obj, "obj", "foo", "bar");
        throw new UnsupportedOperationException();
    }

    protected String getEncoding() {
        return encoding;
    }

    protected void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    protected String getSchemaLocation() {
        return schemaLocation;
    }

    protected void setSchemaLocation(String location) {
        schemaLocation = location;
    }

    protected String getNoNSSchemaLocation() {
        return noNSSchemaLocation;
    }

    protected void setNoNSSchemaLocation(String location) {
        noNSSchemaLocation = location;
    }

    protected boolean isFormattedOutput() {
        return formattedOutput;
    }

    protected void setFormattedOutput(boolean v) {
        formattedOutput = v;
    }

    protected boolean isFragment() {
        return fragment;
    }

    protected void setFragment(boolean v) {
        fragment = v;
    }

    protected String getJavaEncoding(String encoding) throws UnsupportedEncodingException {
        try {
            "dummy".getBytes(encoding);
            return encoding;
        }
        catch (UnsupportedEncodingException e) {
        }
        for (int i = 0; i < aliases.length; i += 2) {
            if (encoding.equals(aliases[i])) {
                "dummy".getBytes(aliases[i + 1]);
                return aliases[i + 1];
            }
        }
        throw new UnsupportedEncodingException(encoding);
    }

    public void setProperty(String name, Object value) throws PropertyException {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }
        if (JAXB_ENCODING.equals(name)) {
            checkString(name, value);
            setEncoding((String) value);
        } else if (JAXB_FORMATTED_OUTPUT.equals(name)) {
            checkBoolean(name, value);
            setFormattedOutput(((Boolean) value).booleanValue());
        } else if (JAXB_NO_NAMESPACE_SCHEMA_LOCATION.equals(name)) {
            checkString(name, value);
            setNoNSSchemaLocation((String) value);
        } else if (JAXB_SCHEMA_LOCATION.equals(name)) {
            checkString(name, value);
            setSchemaLocation((String) value);
        } else if (JAXB_FRAGMENT.equals(name)) {
            checkBoolean(name, value);
            setFragment(((Boolean) value).booleanValue());
        } else {
            throw new PropertyException(name, value);
        }
    }

    public Object getProperty(String name) throws PropertyException {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }
        if (JAXB_ENCODING.equals(name)) {
            return getEncoding();
        } else if (JAXB_FORMATTED_OUTPUT.equals(name)) {
            return isFormattedOutput() ? Boolean.TRUE : Boolean.FALSE;
        } else if (JAXB_NO_NAMESPACE_SCHEMA_LOCATION.equals(name)) {
            return getNoNSSchemaLocation();
        } else if (JAXB_SCHEMA_LOCATION.equals(name)) {
            return getSchemaLocation();
        } else if (JAXB_FRAGMENT.equals(name)) {
            return isFragment() ? Boolean.TRUE : Boolean.FALSE;
        } else {
            throw new PropertyException(name);
        }
    }

    public ValidationEventHandler getEventHandler() throws JAXBException {
        return eventHandler;
    }

    public void setEventHandler(ValidationEventHandler handler) throws JAXBException {
        if (handler == null) {
            eventHandler = new DefaultValidationEventHandler();
        } else {
            eventHandler = handler;
        }
    }

    private void checkBoolean(String name, Object value) throws PropertyException {
        if (!(value instanceof Boolean)) {
            throw new PropertyException(name + " must be a boolean");
        }
    }

    private void checkString(String name, Object value) throws PropertyException {
        if (!(value instanceof String)) {
            throw new PropertyException(name + " must be a string");
        }
    }

    private void checkNotNull(Object o1, String o1Name, Object o2, String o2Name) {
        if (o1 == null) {
            throw new IllegalArgumentException(o1Name + " must not be null");
        }
        if (o2 == null) {
            throw new IllegalArgumentException(o2Name + " must not be null");
        }
    }

    public void marshal(Object obj, XMLEventWriter writer)
            throws JAXBException {
        throw new UnsupportedOperationException();
    }

    public void marshal(Object obj, XMLStreamWriter writer) throws JAXBException {
        throw new UnsupportedOperationException();
    }

    public void setSchema(Schema schema) {
        throw new UnsupportedOperationException();
    }

    public Schema getSchema() {
        throw new UnsupportedOperationException();
    }

    public void setAdapter(XmlAdapter adapter) {
        if (adapter == null) {
            throw new IllegalArgumentException();
        }
        setAdapter((Class<XmlAdapter>) adapter.getClass(), adapter);
    }

    public <A extends XmlAdapter> void setAdapter(Class<A> type, A adapter) {
        throw new UnsupportedOperationException();
    }

    public <A extends XmlAdapter> A getAdapter(Class<A> type) {
        throw new UnsupportedOperationException();
    }

    public void setAttachmentMarshaller(AttachmentMarshaller am) {
        throw new UnsupportedOperationException();
    }

    public AttachmentMarshaller getAttachmentMarshaller() {
        throw new UnsupportedOperationException();
    }

    public void setListener(javax.xml.bind.Marshaller.Listener listener) {
        throw new UnsupportedOperationException();
    }

    public javax.xml.bind.Marshaller.Listener getListener() {
        throw new UnsupportedOperationException();
    }
}
