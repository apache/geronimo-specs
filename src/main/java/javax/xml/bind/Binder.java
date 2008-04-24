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
package javax.xml.bind;

import javax.xml.validation.Schema;

public abstract class Binder<XmlNode> {

    public abstract ValidationEventHandler getEventHandler() throws JAXBException;

    public abstract Object getJAXBNode(XmlNode xmlNode);

    public abstract Object getProperty(String name) throws PropertyException;

    public abstract Schema getSchema();

    public abstract XmlNode getXMLNode(Object jaxbObject);

    public abstract void marshal(Object jaxbObject, XmlNode xmlNode) throws JAXBException;

    public abstract void setEventHandler(ValidationEventHandler handler) throws JAXBException;

    public abstract void setProperty(String name, Object value) throws PropertyException;

    public abstract void setSchema(Schema schema);

    public abstract Object unmarshal(XmlNode xmlNode) throws JAXBException;

    public abstract <T> JAXBElement<T> unmarshal(XmlNode xmlNode, Class<T> declaredType) throws JAXBException;

    public abstract Object updateJAXB(XmlNode xmlNode) throws JAXBException;

    public abstract XmlNode updateXML(Object jaxbObject) throws JAXBException;

    public abstract XmlNode updateXML(Object jaxbObject, XmlNode xmlNode) throws JAXBException; 

    
}
