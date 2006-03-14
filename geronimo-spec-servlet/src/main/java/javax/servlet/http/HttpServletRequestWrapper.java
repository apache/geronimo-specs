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

import javax.servlet.ServletRequestWrapper;

/**
 * @version $Revision$ $Date$
 */
public class HttpServletRequestWrapper extends ServletRequestWrapper implements
        HttpServletRequest {
    private HttpServletRequest request;

    public HttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        super.setRequest(request);
        this.request = request;
    }

    public String getAuthType() {
        return request.getAuthType();
    }

    public String getContextPath() {
        return request.getContextPath();
    }

    public Cookie[] getCookies() {
        return request.getCookies();
    }

    public long getDateHeader(String name) {
        return request.getDateHeader(name);
    }

    public String getHeader(String name) {
        return request.getHeader(name);
    }

    public Enumeration getHeaderNames() {
        return request.getHeaderNames();
    }

    public Enumeration getHeaders(String name) {
        return request.getHeaders(name);
    }

    public int getIntHeader(String name) {
        return request.getIntHeader(name);
    }

    public String getMethod() {
        return request.getMethod();
    }

    public String getPathInfo() {
        return request.getPathInfo();
    }

    public String getPathTranslated() {
        return request.getPathTranslated();
    }

    public String getQueryString() {
        return request.getQueryString();
    }

    public String getRemoteUser() {
        return request.getRemoteUser();
    }

    public String getRequestedSessionId() {
        return request.getRequestedSessionId();
    }

    public String getRequestURI() {
        return request.getRequestURI();
    }

    public StringBuffer getRequestURL() {
        return request.getRequestURL();
    }

    public String getServletPath() {
        return request.getServletPath();
    }

    public HttpSession getSession() {
        return request.getSession();
    }

    public HttpSession getSession(boolean create) {
        return request.getSession(create);
    }

    public Principal getUserPrincipal() {
        return request.getUserPrincipal();
    }

    public boolean isRequestedSessionIdFromCookie() {
        return request.isRequestedSessionIdFromCookie();
    }

    @SuppressWarnings("deprecation")
    public boolean isRequestedSessionIdFromUrl() {
        // this method is deprecated in the request
        // but not deprecated in the wrapper - not sure why
        return request.isRequestedSessionIdFromUrl();
    }

    public boolean isRequestedSessionIdFromURL() {
        return request.isRequestedSessionIdFromURL();
    }

    public boolean isRequestedSessionIdValid() {
        return request.isRequestedSessionIdValid();
    }

    public boolean isUserInRole(String role) {
        return request.isUserInRole(role);
    }

}
