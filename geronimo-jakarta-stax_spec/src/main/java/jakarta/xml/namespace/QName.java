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
package jakarta.xml.namespace;

import java.io.Serializable;
import javax.xml.XMLConstants;

public class QName implements Serializable {
    // not specified in the javadoc, but this is what appears to be used.
    private static final long serialVersionUID = -9120448754896609940L;
    // the namespace URI of this qualified name
    private final String namespaceURI;
    // the local part of the name
    private final String localPart;
    // the name prefix string
    private final String prefix;

    public QName(String localPart) {
        // default both the URI and the prefix
        this("", localPart, XMLConstants.DEFAULT_NS_PREFIX);
    }

    public QName(String namespaceURI, String localPart) {
        // the default prefix is defined in XMLConstants
        this(namespaceURI, localPart, XMLConstants.DEFAULT_NS_PREFIX);
    }

    public QName(String namespaceURI, String localPart, String prefix) {
        // there is a default in constants as well
        this.namespaceURI = namespaceURI == null ? "" : namespaceURI;

        // both the local part and the prefix are required
        if (localPart == null) {
            throw new IllegalArgumentException("local part is required when creating a QName");
        }
        this.localPart = localPart;

        if (prefix == null) {
            throw new IllegalArgumentException("prefix is required when creating a QName");
        }
        this.prefix = prefix;
    }

    public final boolean equals(Object other) {
        if (other == null || !(other instanceof QName)) {
            return false;
        }

        QName qName = (QName)other;

        // only the namespace and localPart are considered.  the prefix is not used.
        return namespaceURI.equals(qName.namespaceURI) && localPart.equals(qName.localPart);
    }

    public final int hashCode() {
        // uses both the namespace and localpart as a combined hash.
        return namespaceURI.hashCode() ^ localPart.hashCode();
    }

    public String getNamespaceURI() {
        return namespaceURI;
    }

    public String getLocalPart() {
        return localPart;
    }

    public String getPrefix() {
        return prefix;
    }

    public String toString() {
        // if no real URI, this is just the local part, otherwise
        // use the braces syntax.
        if (namespaceURI.length() == 0) {
            return localPart;
        }
        else {
            return "{" + namespaceURI + "}" + localPart;
        }
    }

    public static QName valueOf(String source) {
        // a name is required
        if (source == null) {
            throw new IllegalArgumentException("source QName string is required");
        }

        // if this is a null string or it does not start with '{', treat this
        // as a local part alone.
        if (source.length() == 0 || source.charAt(0) != '{') {
            return new QName(source);
        }

        // Namespace URI and local part specified
        int uriEnd  = source.indexOf('}');
        if (uriEnd == -1) {
            throw new IllegalArgumentException("Incorrect QName syntax: " + source);
        }

        String uri = source.substring(1, uriEnd);
        if (uri.length() == 0) {
            throw new IllegalArgumentException("Null namespace URI in QName value: " + source);
        }
        // no prefix possible here, just the URI and the local part
        return new QName(uri, source.substring(uriEnd + 1));
    }
}
