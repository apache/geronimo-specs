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
package jakarta.xml.stream;

import jakarta.xml.stream.util.XMLEventAllocator;

public abstract class XMLInputFactory {
    public static final java.lang.String ALLOCATOR = "jakarta.xml.stream.allocator";
    public static final java.lang.String IS_COALESCING = "jakarta.xml.stream.isCoalescing";
    public static final java.lang.String IS_NAMESPACE_AWARE = "jakarta.xml.stream.isNamespaceAware";
    public static final java.lang.String IS_REPLACING_ENTITY_REFERENCES = "jakarta.xml.stream.isReplacingEntityReferences";
    public static final java.lang.String IS_SUPPORTING_EXTERNAL_ENTITIES = "jakarta.xml.stream.isSupportingExternalEntities";
    public static final java.lang.String IS_VALIDATING = "jakarta.xml.stream.isValidating";
    public static final java.lang.String REPORTER = "jakarta.xml.stream.reporter";
    public static final java.lang.String RESOLVER = "jakarta.xml.stream.resolver";
    public static final java.lang.String SUPPORT_DTD = "jakarta.xml.stream.supportDTD";

    protected XMLInputFactory() {
    }

    public static XMLInputFactory newInstance()
            throws FactoryConfigurationError {
        // We'll assume the XMLInputFactory from the RI as a backup.
        return (XMLInputFactory)FactoryLocator.locate("jakarta.xml.stream.XMLInputFactory", "com.ctc.wstx.stax.WstxInputFactory");
    }

    public static XMLInputFactory newFactory()
            throws FactoryConfigurationError {
        // We'll assume the XMLInputFactory from the RI as a backup.
        return (XMLInputFactory)FactoryLocator.locate("jakarta.xml.stream.XMLInputFactory", "com.ctc.wstx.stax.WstxInputFactory");
    }


    /**
     * Create a new XMLInputFactory
     *
     * @deprecated to maintain API consistency.  All newInstance methods are
     * replaced with corresponding newFactory methods.  The replacement
     * newFactory(String factoryId, ClassLoader classLoader)
     * method defines no changes in behavior from this method.
     */
    public static XMLInputFactory newInstance(java.lang.String factoryId,
            java.lang.ClassLoader classLoader) throws FactoryConfigurationError {
        // We'll assume the XMLInputFactory from the RI as a backup.
        return (XMLInputFactory)FactoryLocator.locate(factoryId, "com.ctc.wstx.stax.WstxInputFactory", classLoader);
    }


    public static XMLInputFactory newFactory(java.lang.String factoryId,
            java.lang.ClassLoader classLoader) throws FactoryConfigurationError {
        // We'll assume the XMLInputFactory from the RI as a backup.
        return (XMLInputFactory)FactoryLocator.locate(factoryId, "com.ctc.wstx.stax.WstxInputFactory", classLoader);
    }

    public abstract XMLStreamReader createXMLStreamReader(java.io.Reader reader)
            throws XMLStreamException;

    public abstract XMLStreamReader createXMLStreamReader(
            javax.xml.transform.Source source) throws XMLStreamException;

    public abstract XMLStreamReader createXMLStreamReader(
            java.io.InputStream stream) throws XMLStreamException;

    public abstract XMLStreamReader createXMLStreamReader(
            java.io.InputStream stream, java.lang.String encoding)
            throws XMLStreamException;

    public abstract XMLStreamReader createXMLStreamReader(
            java.lang.String systemId, java.io.InputStream stream)
            throws XMLStreamException;

    public abstract XMLStreamReader createXMLStreamReader(
            java.lang.String systemId, java.io.Reader reader)
            throws XMLStreamException;

    public abstract XMLEventReader createXMLEventReader(java.io.Reader reader)
            throws XMLStreamException;

    public abstract XMLEventReader createXMLEventReader(
            java.lang.String systemId, java.io.Reader reader)
            throws XMLStreamException;

    public abstract XMLEventReader createXMLEventReader(XMLStreamReader reader)
            throws XMLStreamException;

    public abstract XMLEventReader createXMLEventReader(
            javax.xml.transform.Source source) throws XMLStreamException;

    public abstract XMLEventReader createXMLEventReader(
            java.io.InputStream stream) throws XMLStreamException;

    public abstract XMLEventReader createXMLEventReader(
            java.io.InputStream stream, java.lang.String encoding)
            throws XMLStreamException;

    public abstract XMLEventReader createXMLEventReader(
            java.lang.String systemId, java.io.InputStream stream)
            throws XMLStreamException;

    public abstract XMLStreamReader createFilteredReader(
            XMLStreamReader reader, StreamFilter filter)
            throws XMLStreamException;

    public abstract XMLEventReader createFilteredReader(XMLEventReader reader,
            EventFilter filter) throws XMLStreamException;

    public abstract XMLResolver getXMLResolver();

    public abstract void setXMLResolver(XMLResolver resolver);

    public abstract XMLReporter getXMLReporter();

    public abstract void setXMLReporter(XMLReporter reporter);

    public abstract void setProperty(java.lang.String name,
            java.lang.Object value) throws java.lang.IllegalArgumentException;

    public abstract java.lang.Object getProperty(java.lang.String name)
            throws java.lang.IllegalArgumentException;

    public abstract boolean isPropertySupported(java.lang.String name);

    public abstract void setEventAllocator(XMLEventAllocator allocator);

    public abstract XMLEventAllocator getEventAllocator();
}
