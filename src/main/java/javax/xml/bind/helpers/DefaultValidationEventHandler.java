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

import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;

import org.w3c.dom.Node;

public class DefaultValidationEventHandler implements ValidationEventHandler {

    public boolean handleEvent(ValidationEvent event) {
        if (event == null) {
            throw new IllegalArgumentException();
        }
        String severity = null;
        boolean retVal = false;
        switch(event.getSeverity()) {
        case ValidationEvent.WARNING:
            severity = "[WARNING]: ";
            retVal = true;
            break;

        case ValidationEvent.ERROR:
            severity = "[ERROR]: ";
            retVal = false;
            break;

        case ValidationEvent.FATAL_ERROR:
            severity = "[FATAL_ERROR]: ";
            retVal = false;
            break;
        }
        String location = getLocation(event);
        System.out.println("DefaultValidationEventHandler " + severity + " " + event.getMessage() + "\n     Location: " + location);
        return retVal;
    }

    private String getLocation(ValidationEvent event) {
        StringBuffer msg = new StringBuffer();
        ValidationEventLocator locator = event.getLocator();
        if (locator != null) {
            URL url = locator.getURL();
            Object obj = locator.getObject();
            Node node = locator.getNode();
            int line = locator.getLineNumber();
            if(url != null || line != -1) {
                msg.append("line ").append(line);
                if(url != null) {
                    msg.append(" of ").append(url);
                }
            } else {
                if (obj != null) {
                    msg.append(" obj: ").append(obj.toString());
                } else if(node != null) {
                    msg.append(" node: ").append(node.toString());
                }
            }
        } else {
            msg.append("unavailable");
        }
        return msg.toString();
    }

}
