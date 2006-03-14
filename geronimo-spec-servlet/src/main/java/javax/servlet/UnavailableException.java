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
public class UnavailableException extends ServletException implements
        Serializable {
    private static final long serialVersionUID = -8808724454979990506L;
    private int unavalibleSeconds;
    private Servlet servlet;
    private boolean permanent;

    @Deprecated
    public UnavailableException(int seconds, Servlet servlet, String msg) {
        this(msg, seconds);
        this.servlet = servlet;
        permanent = false;
    }

    @Deprecated
    public UnavailableException(Servlet servlet, String msg) {
        this(msg);
        this.servlet = servlet;
        permanent = true;
        unavalibleSeconds = -1;
    }

    public UnavailableException(String msg) {
        super(msg);
        permanent = true;
        unavalibleSeconds = -1;
    }

    public UnavailableException(String msg, int seconds) {
        super(msg);
        if (seconds < 0) {
            unavalibleSeconds = -1;
        } else {
            unavalibleSeconds = seconds;
        }
        permanent = false;
    }

    @Deprecated
    public Servlet getServlet() {
        return servlet;
    }

    public int getUnavailableSeconds() {
        return unavalibleSeconds;
    }

    public boolean isPermanent() {
        return permanent;
    }
}
