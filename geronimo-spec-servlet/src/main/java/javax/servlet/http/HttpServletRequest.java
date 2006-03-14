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
package javax.servlet.http;

import java.security.Principal;
import java.util.Enumeration;

import javax.servlet.ServletRequest;

/**
 * @version $Revision$ $Date$
 */
public interface HttpServletRequest extends ServletRequest {
    public static final String BASIC_AUTH = "BASIC";
    public static final String CLIENT_CERT_AUTH = "CLIENT_CERT";
    public static final String DIGEST_AUTH = "DIGEST";
    public static final String FORM_AUTH = "FORM";

    public String getAuthType();

    public String getContextPath();

    public Cookie[] getCookies();

    public long getDateHeader(String name);

    public String getHeader(String name);

    public Enumeration getHeaderNames();

    public Enumeration getHeaders(String name);

    public int getIntHeader(String name);

    public String getMethod();

    public String getPathInfo();

    public String getPathTranslated();

    public String getQueryString();

    public String getRemoteUser();

    public String getRequestedSessionId();

    public String getRequestURI();

    public StringBuffer getRequestURL();

    public String getServletPath();

    public HttpSession getSession();

    public HttpSession getSession(boolean create);

    public Principal getUserPrincipal();

    public boolean isRequestedSessionIdFromCookie();

    @Deprecated
    public boolean isRequestedSessionIdFromUrl();

    public boolean isRequestedSessionIdFromURL();

    public boolean isRequestedSessionIdValid();

    public boolean isUserInRole(String role);

}
