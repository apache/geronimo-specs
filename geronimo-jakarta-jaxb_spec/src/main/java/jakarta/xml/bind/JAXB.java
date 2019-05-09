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
package javax.xml.bind;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.io.IOException;
import java.io.Reader;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URISyntaxException;
import java.net.MalformedURLException;
import java.beans.Introspector;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

final public class JAXB {

    private JAXB() {
    }

    public static void marshal(Object object, File file) {
        if (file == null) {
            throw new IllegalStateException("No file is given");
        }
        marshal(object, new StreamResult(file));
    }

    public static void marshal(Object object, OutputStream os) {
        if (os == null) {
            throw new IllegalStateException("No output stream is given");
        }
        marshal(object, new StreamResult(os));
    }

    public static void marshal(Object object, Writer writer) {
        if (writer == null) {
            throw new IllegalStateException("No writer is given");
        }
        marshal(object, new StreamResult(writer));
    }

    public static void marshal(Object object, String str) {
        if (str == null) {
            throw new IllegalStateException("No string destination is given");
        }
        try {
            marshal(object, new URI(str));
        } catch (URISyntaxException e) {
            marshal(object, new File(str));
        }
    }

    public static void marshal(Object object, URI uri) {
        if (uri == null) {
            throw new IllegalStateException("No uri is given");
        }
        try {
            marshal(object, uri.toURL());
        } catch (IOException e) {
            throw new DataBindingException(e);
        }
    }

    public static void marshal(Object object, URL url) {
        if (url == null) {
            throw new IllegalStateException("No url is given");
        }
        try {
            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(false);
            con.connect();
            marshal(object, new StreamResult(con.getOutputStream()));
        } catch (IOException e) {
            throw new DataBindingException(e);
        }
    }

    public static void marshal(Object object, Result result) {
        try {
            JAXBContext context;
            if (object instanceof JAXBElement) {
                context = getContext(((JAXBElement<?>) object).getDeclaredType());
            } else {
                Class<?> clazz = object.getClass();
                XmlRootElement r = clazz.getAnnotation(XmlRootElement.class);
                if (r == null) {
                    // we need to infer the name
                    object = new JAXBElement(new QName(Introspector.decapitalize(clazz.getSimpleName())),
                                             clazz, object);
                }
                context = getContext(clazz);
            }
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
            m.marshal(object, result);
        } catch (JAXBException e) {
            throw new DataBindingException(e);
        }
    }

    public static <T> T unmarshal(File file, Class<T> type) {
        if (file == null) {
            throw new IllegalStateException("No file is given");
        }
        return unmarshal(new StreamSource(file), type);
    }

    public static <T> T unmarshal(URL url, Class<T> type) {
        if (url == null) {
            throw new IllegalStateException("No url is given");
        }
        return unmarshal(new StreamSource(url.toExternalForm()), type);
    }

    public static <T> T unmarshal(URI uri, Class<T> type) {
        if (uri == null) {
            throw new IllegalStateException("No uri is given");
        }
        try {
            return unmarshal(uri.toURL(), type);
        } catch (MalformedURLException e) {
            throw new DataBindingException(e);
        }
    }

    public static <T> T unmarshal(String str, Class<T> type) {
        if (str == null) {
            throw new IllegalStateException("No string destination is given");
        }
        try {
            return unmarshal(new URI(str), type);
        } catch (URISyntaxException e) {
            return unmarshal(new File(str), type);
        }
    }

    public static <T> T unmarshal(InputStream is, Class<T> type) {
        if (is == null) {
            throw new IllegalStateException("No input stream is given");
        }
        return unmarshal(new StreamSource(is), type);
    }

    public static <T> T unmarshal(Reader reader, Class<T> type) {
        if (reader == null) {
            throw new IllegalStateException("No reader is given");
        }
        return unmarshal(new StreamSource(reader), type);
    }

    public static <T> T unmarshal(Source source, Class<T> type) {
        try {
            JAXBElement<T> item = getContext(type).createUnmarshaller().unmarshal(source, type);
            return item.getValue();
        } catch (JAXBException e) {
            throw new DataBindingException(e);
        }
    }

    private static <T> JAXBContext getContext(Class<T> type) throws JAXBException {
        return JAXBContext.newInstance(type);
    }


}
