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

import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;

/**
 * @version $Revision$ $Date$
 */
public abstract class GenericServlet implements Servlet, ServletConfig,
        Serializable {
    private transient ServletConfig config;

    public GenericServlet() {
        super();
    }

    public void destroy() {
    }

    public String getInitParameter(String name) {
        return config.getInitParameter(name);
    }

    public Enumeration getInitParameterNames() {
        return config.getInitParameterNames();
    }

    public ServletConfig getServletConfig() {
        return config;
    }

    public ServletContext getServletContext() {
        return config.getServletContext();
    }

    public String getServletInfo() {
        return "";
    }

    public String getServletName() {
        return config.getServletName();
    }

    public void init() throws ServletException {
    }

    public void init(ServletConfig config) throws ServletException {
        this.config = config;
        init();
    }

    public void log(String msg) {
        config.getServletContext().log(msg);
    }

    public void log(String msg, Throwable t) {
        config.getServletContext().log(msg, t);
    }

    public abstract void service(ServletRequest req, ServletResponse res)
            throws ServletException, IOException;
}
