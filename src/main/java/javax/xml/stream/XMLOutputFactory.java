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
package javax.xml.stream;

public abstract class XMLOutputFactory {
	public static final String IS_REPAIRING_NAMESPACES = "javax.xml.stream.isRepairingNamespaces";

	protected XMLOutputFactory() { }

	public static XMLOutputFactory newInstance() throws FactoryConfigurationError {
		return (XMLOutputFactory) FactoryLocator.locate("javax.xml.stream.XMLOutputFactory", "com.ctc.wstx.stax.WstxOutputFactory");
	}

	/**
	 * Create a new XMLOutputFactory
	 *
	 * @deprecated This method has been deprecated because
     * it returns an instance of XMLInputFactory, which is of the
     * wrong class.  Use the new method
     * newFactory(java.lang.String factoryId,java.lang.ClassLoader classLoader)
     * instead.
	 */
	public static XMLInputFactory newInstance(String factoryId,
			java.lang.ClassLoader classLoader) throws FactoryConfigurationError {
		return (XMLInputFactory) FactoryLocator.locate(factoryId, "com.ctc.wstx.stax.WstxOutputFactory", classLoader);

	}


	/**
	 * Create a new XMLOutputFactory
	 *
	 * This is the replacement for the deprecated newInstance() method
	 */
	public static XMLOutputFactory newFactory()	throws FactoryConfigurationError {
		return (XMLOutputFactory) FactoryLocator.locate("javax.xml.stream.XMLOutputFactory", "com.ctc.wstx.stax.WstxOutputFactory");
	}

	/**
	 * Create a new XMLOutputFactory
	 *
	 * This is the replacement for the deprecated newInstance() method
	 */
	public static XMLOutputFactory newFactory(String factoryId,	ClassLoader classLoader)
			throws FactoryConfigurationError {
		// essentially the same thing as deprecated newInstance(), but the correct return type.
		return (XMLOutputFactory) FactoryLocator.locate(factoryId, "com.ctc.wstx.stax.WstxOutputFactory", classLoader);
	}

	public abstract XMLStreamWriter createXMLStreamWriter(java.io.Writer stream)
			throws XMLStreamException;

	public abstract XMLStreamWriter createXMLStreamWriter(
			java.io.OutputStream stream) throws XMLStreamException;

	public abstract XMLStreamWriter createXMLStreamWriter(
			java.io.OutputStream stream, String encoding)
			throws XMLStreamException;

	public abstract XMLStreamWriter createXMLStreamWriter(
			javax.xml.transform.Result result) throws XMLStreamException;

	public abstract XMLEventWriter createXMLEventWriter(
			javax.xml.transform.Result result) throws XMLStreamException;

	public abstract XMLEventWriter createXMLEventWriter(
			java.io.OutputStream stream) throws XMLStreamException;

	public abstract XMLEventWriter createXMLEventWriter(
			java.io.OutputStream stream, String encoding)
			throws XMLStreamException;

	public abstract XMLEventWriter createXMLEventWriter(java.io.Writer stream)
			throws XMLStreamException;

	public abstract void setProperty(String name, Object value)
			throws IllegalArgumentException;

	public abstract Object getProperty(String name)
			throws IllegalArgumentException;

	public abstract boolean isPropertySupported(String name);
}
