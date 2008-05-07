/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package javax.xml.bind;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

public class JAXBException extends Exception {

    private static final long serialVersionUID = 0x5dd94775L;

    private String errorCode;
    private Throwable linkedException;

    public JAXBException(String message) {
        this(message, null, null);
    }

    public JAXBException(String message, String errorCode) {
        this(message, errorCode, null);
    }

    public JAXBException(String message, String errorCode, Throwable cause) {
        super(message);
        this.errorCode = errorCode;
        this.linkedException = cause;
    }

    public JAXBException(String message, Throwable cause) {
        this(message, null, cause);
    }

    public JAXBException(Throwable cause) {
        this(null, null, cause);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Throwable getLinkedException() {
        return getCause();
    }

    public synchronized void setLinkedException(Throwable linkedException) {
        this.linkedException = linkedException;
    }

    public String toString() {
        return linkedException != null ?
                super.toString() + "\n - with linked exception:\n[" + linkedException.toString() + "]" : 
                super.toString();
    }

    @Override
    public Throwable getCause() {
        return linkedException;
    }

    public void printStackTrace() {
        super.printStackTrace();
    }

    public void printStackTrace(PrintStream ps) {
        super.printStackTrace(ps);
    }

    public void printStackTrace(PrintWriter pw) {
        super.printStackTrace(pw);
    }

}
