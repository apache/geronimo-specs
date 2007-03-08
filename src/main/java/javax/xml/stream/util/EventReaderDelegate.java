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

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public class EventReaderDelegate implements XMLEventReader {
private XMLEventReader r;

    public EventReaderDelegate()
    {
    }
    
    public EventReaderDelegate(XMLEventReader r)
    {
        this.r = r;
    }

    public void close()  throws XMLStreamException {
        r.close();
    }
    
    public String getElementText()  throws XMLStreamException {
        return r.getElementText();
    }
    
    public XMLEventReader getParent() {
        return r;
    }
    
    public Object getProperty(java.lang.String name) throws java.lang.IllegalArgumentException {
        return r.getProperty(name);
   }
   
    public boolean hasNext() {
        return r.hasNext();
    }
    
    public Object next() {
        return r.next();
    }
    
    public XMLEvent nextEvent() throws XMLStreamException {
        return r.nextEvent();
    }
    
    public XMLEvent nextTag()  throws XMLStreamException {
        return r.nextTag();
    }
    
    public XMLEvent peek()  throws XMLStreamException {
        return r.peek();
    }
    
    public void remove() {
        r.remove();
    }
    
    public void setParent(XMLEventReader reader) {
        r.setParent(reader);
    }
}