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
package javax.xml.bind.util;

import java.util.ArrayList;

import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEvent;

public class ValidationEventCollector implements ValidationEventHandler {

    private ArrayList<ValidationEvent> events = new ArrayList<ValidationEvent>();

    public ValidationEvent[] getEvents() {
        return events.toArray(new ValidationEvent[events.size()]);
    }

    public boolean handleEvent(ValidationEvent event) {
        events.add(event);
        return event.getSeverity() != ValidationEvent.FATAL_ERROR;
    }

    public boolean hasEvents() {
        return !events.isEmpty();
    }

    public void reset() {
        events.clear();
    }
}
