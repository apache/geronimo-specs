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
package javax.xml.bind.helpers;

import java.net.URL;
import java.net.MalformedURLException;

import javax.xml.bind.ValidationEventLocator;

import org.w3c.dom.Node;

import org.xml.sax.SAXParseException;
import org.xml.sax.Locator;

public class ValidationEventLocatorImpl implements ValidationEventLocator {

    private URL url;
    private int offset = -1;
    private int lineNumber = -1;
    private int columnNumber = -1;
    private Object object;
    private Node node;

    public ValidationEventLocatorImpl() {
    }

    public ValidationEventLocatorImpl(Locator loc) {
        if (loc == null) {
            throw new IllegalArgumentException("loc must not be null");
        }
        url = toURL(loc.getSystemId());
        columnNumber = loc.getColumnNumber();
        lineNumber = loc.getLineNumber();
    }

    public ValidationEventLocatorImpl(SAXParseException e) {
        if (e == null) {
            throw new IllegalArgumentException("e must not be null");
        }
        url = toURL(e.getSystemId());
        columnNumber = e.getColumnNumber();
        lineNumber = e.getLineNumber();
    }

    public ValidationEventLocatorImpl(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("node must not be null");
        }
        this.node = node;
    }

    public ValidationEventLocatorImpl(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("object must not be null");
        }
        this.object = object;
    }

    private static URL toURL(String systemId) {
        try {
            return new URL(systemId);
        }
        catch (MalformedURLException e) {
            return null;
        }
    }

    public URL getURL() {
        return url;
    }

    public void setURL(URL url) {
        this.url = url;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public String toString() {
        return "[node=" + getNode() + ", " +
                "object=" + getObject() + ", " +
                "url=" + getURL() + ", " +
                "line=" + String.valueOf(getLineNumber()) + "," +
                "col=" + String.valueOf(getColumnNumber()) + "," +
                "offset=" + String.valueOf(getOffset()) + "]";
    }

}
