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
package javax.xml.stream.util;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class StreamReaderDelegate implements XMLStreamReader {
	XMLStreamReader r;

	public StreamReaderDelegate() {
	}

	public StreamReaderDelegate(XMLStreamReader reader) {
		r = reader;
	}

	public void close() throws XMLStreamException {
		r.close();
	}

	public int getAttributeCount() {
		return r.getAttributeCount();
	}

	public String getAttributeLocalName(int index) {
		return r.getAttributeLocalName(index);
	}

	public QName getAttributeName(int index) {
		return r.getAttributeName(index);
	}

	public String getAttributeNamespace(int index) {
		return r.getAttributeNamespace(index);
	}

	public String getAttributePrefix(int index) {
		return r.getAttributePrefix(index);
	}

	public String getAttributeType(int index) {
		return r.getAttributeType(index);
	}

	public String getAttributeValue(int index) {
		return r.getAttributeValue(index);
	}

	public String getAttributeValue(String namespaceURI, String localName) {
		return r.getAttributeValue(namespaceURI, localName);
	}

	public String getCharacterEncodingScheme() {
		return r.getCharacterEncodingScheme();
	}

	public String getElementText() throws XMLStreamException {
		return r.getElementText();
	}

	public String getEncoding() {
		return r.getEncoding();
	}

	public int getEventType() {
		return r.getEventType();
	}

	public String getLocalName() {
		return r.getLocalName();
	}

	public Location getLocation() {
		return r.getLocation();
	}

	public QName getName() {
		return r.getName();
	}

	public NamespaceContext getNamespaceContext() {
		return r.getNamespaceContext();
	}

	public int getNamespaceCount() {
		return r.getNamespaceCount();
	}

	public String getNamespacePrefix(int index) {
		return r.getNamespacePrefix(index);
	}

	public String getNamespaceURI() {
		return r.getNamespaceURI();
	}

	public String getNamespaceURI(int index) {
		return r.getNamespaceURI(index);
	}

	public String getNamespaceURI(String prefix) {
		return r.getNamespaceURI(prefix);
	}

	public String getPIData() {
		return r.getPIData();
	}

	public String getPITarget() {
		return r.getPITarget();
	}

	public String getPrefix() {
		return r.getPrefix();
	}

	public Object getProperty(String name) throws IllegalArgumentException {
		return r.getProperty(name);
	}

	public String getText() {
		return r.getText();
	}

	public char[] getTextCharacters() {
		return r.getTextCharacters();
	}

	public int getTextCharacters(int sourceStart, char[] target,
			int targetStart, int length) throws XMLStreamException {
		return r.getTextCharacters(sourceStart, target, targetStart, length);
	}

	public int getTextLength() {
		return r.getTextLength();
	}

	public int getTextStart() {
		return r.getTextStart();
	}

	public String getVersion() {
		return r.getVersion();
	}

	public boolean hasName() {
		return r.hasName();
	}

	public boolean hasNext() throws XMLStreamException {
		return r.hasNext();
	}

	public boolean hasText() {
		return r.hasText();
	}

	public boolean isAttributeSpecified(int index) {
		return r.isAttributeSpecified(index);
	}

	public boolean isCharacters() {
		return r.isCharacters();
	}

	public boolean isEndElement() {
		return r.isEndElement();
	}

	public boolean isStandalone() {
		return r.isStandalone();
	}

	public boolean isStartElement() {
		return r.isStartElement();
	}

	public boolean isWhiteSpace() {
		return r.isWhiteSpace();
	}

	public int next() throws XMLStreamException {
		return r.next();
	}

	public int nextTag() throws XMLStreamException {
		return r.nextTag();
	}

	public void require(int type, String namespaceURI, String localName)
			throws XMLStreamException {
		r.require(type, namespaceURI, localName);
	}

	public boolean standaloneSet() {
		return r.standaloneSet();
	}
}
