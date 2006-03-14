/*
 * Copyright 2006 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 */

//
// This source code implements specifications defined by the Java
// Community Process. In order to remain compliant with the specification
// DO NOT add / change / or delete method signatures!
//
package javax.servlet;

import java.io.Serializable;

/**
 * @version $Revision$ $Date$
 */
public class ServletException extends Exception implements Serializable {
    private static final long serialVersionUID = 7639550382612852862L;
    public ServletException() {
        super();
    }
    public ServletException(String msg) {
        super(msg);
    }
    public ServletException(String msg, Throwable rootCause) {
        super(msg, rootCause);
    }
    public ServletException(Throwable rootCause) {
        super(rootCause);
    }
    public Throwable getRootCause() {
        return this.getCause();
    }
}
