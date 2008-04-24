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

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;

public class ValidationEventImpl implements ValidationEvent {

    private int severity;
    private String message;
    private Throwable linkedException;
    private ValidationEventLocator locator;

    public ValidationEventImpl(int severity, String message, ValidationEventLocator locator) {
        this(severity, message, locator, null);
    }

    public ValidationEventImpl(int severity, String message, ValidationEventLocator locator, Throwable linkedException) {
        setSeverity(severity);
        this.message = message;
        this.locator = locator;
        this.linkedException = linkedException;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        if (severity != 0 && severity != 1 && severity != 2) {
            throw new IllegalArgumentException("Illegal severity");
        }
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getLinkedException() {
        return linkedException;
    }

    public void setLinkedException(Throwable linkedException) {
        this.linkedException = linkedException;
    }

    public ValidationEventLocator getLocator() {
        return locator;
    }

    public void setLocator(ValidationEventLocator locator) {
        this.locator = locator;
    }

    public String toString() {
        String s;
        switch (getSeverity()) {
            case WARNING:
                s = "WARNING";
                break;
            case ERROR:
                s = "ERROR";
                break;
            case FATAL_ERROR:
                s = "FATAL_ERROR";
                break;
            default:
                s = String.valueOf(getSeverity());
                break;
        }
        return "[severity=" + s + ", message=" + getMessage() + ", locator=" + getLocator() + "]";
    }

}
