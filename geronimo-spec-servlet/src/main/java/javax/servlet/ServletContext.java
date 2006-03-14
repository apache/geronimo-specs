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

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

/**
 * @version $Revision$ $Date$
 */
public interface ServletContext {
    public Object getAttribute(String name);

    public Enumeration getAttributeNames();

    public ServletContext getContext(String uriPath);
    
    public String getContextPath();

    public String getInitParameter(String name);

    public Enumeration getInitParameterNames();

    public int getMajorVersion();

    public String getMimeType(String file);

    public int getMinorVersion();

    public RequestDispatcher getNamedDispatcher(String name);

    public String getRealPath(String path);

    public RequestDispatcher getRequestDispatcher(String path);

    public URL getResource(String path) throws MalformedURLException;

    public InputStream getResourceAsStream(String path);

    public Set getResourcePaths(String path);

    public String getServerInfo();

    @Deprecated
    public Servlet getServlet(String name);

    public String getServletContextName();

    @Deprecated
    public Enumeration getServletNames();

    @Deprecated
    public Enumeration getServlets();

    @Deprecated
    public void log(Exception e, String msg);

    public void log(String msg);

    public void log(String msg, Throwable t);

    public void removeAttribute(String name);

    public void setAttribute(String name, Object object);
}
