/*
 **
 ** Licensed to the Apache Software Foundation (ASF) under one
 ** or more contributor license agreements.  See the NOTICE file
 ** distributed with this work for additional information
 ** regarding copyright ownership.  The ASF licenses this file
 ** to you under the Apache License, Version 2.0 (the
 ** "License"); you may not use this file except in compliance
 ** with the License.  You may obtain a copy of the License at
 **
 **  http://www.apache.org/licenses/LICENSE-2.0
 **
 ** Unless required by applicable law or agreed to in writing,
 ** software distributed under the License is distributed on an
 ** "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 ** KIND, either express or implied.  See the License for the
 ** specific language governing permissions and limitations
 ** under the License.
 */
package javax.xml.bind.annotation;

import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.Source;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.bind.ValidationEventHandler;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.DocumentFragment;

public class W3CDomHandler implements DomHandler<Element, DOMResult> {

    private DocumentBuilder builder;

    public W3CDomHandler() {
    }

    public W3CDomHandler(DocumentBuilder builder) {
        if (builder == null) {
            throw new IllegalArgumentException();
        }
        this.builder = builder;
    }

    public DOMResult createUnmarshaller(ValidationEventHandler errorHandler) {
        if (builder == null) {
            return new DOMResult();
        } else {
            return new DOMResult(builder.newDocument());
        }
    }

    public DocumentBuilder getBuilder() {
        return builder;
    }

    public Element getElement(DOMResult rt) {
        Node n = rt.getNode();
        if (n instanceof Document) {
            return ((Document)n).getDocumentElement();
        }
        if (n instanceof Element) {
            return (Element)n;
        }
        if (n instanceof DocumentFragment) {
            return (Element)n.getChildNodes().item(0);
        } else {
            throw new IllegalStateException(n.toString());
        }
    }

    public Source marshal(Element n, ValidationEventHandler errorHandler) {
        return new DOMSource(n);
    }

    public void setBuilder(DocumentBuilder builder) {
        this.builder = builder;
    }
}
