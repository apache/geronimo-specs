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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 * @version $Revision$ $Date$
 */
public interface ServletRequest {
    public Object getAttribute(String name);

    public Enumeration getAttributeNames();

    public String getCharacterEncoding();

    public int getContentLength();

    public String getContentType();

    public ServletInputStream getInputStream() throws IOException;

    public String getLocalAddr();

    public Locale getLocale();

    public Enumeration getLocales();

    public String getLocalName();

    public int getLocalPort();

    public String getParameter(String name);

    public Map getParameterMap();

    public Enumeration getParameterNames();

    public String[] getParameterValues(String name);

    public String getProtocol();

    public BufferedReader getReader() throws IOException;

    @Deprecated
    public String getRealPath(String path);

    public String getRemoteAddr();

    public String getRemoteHost();

    public int getRemotePort();

    public RequestDispatcher getRequestDispatcher(String path);

    public String getScheme();

    public String getServerName();

    public int getServerPort();

    public boolean isSecure();

    public void removeAttribute(String name);

    public void setAttribute(String name, Object value);

    public void setCharacterEncoding(String charEncodingName)
            throws UnsupportedEncodingException;

}
