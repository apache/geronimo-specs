/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package javax.security.jacc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.sun.org.apache.bcel.internal.generic.RETURN;

/**
 * @version $Rev$ $Date$
 */
public class MockHttpServletRequest implements HttpServletRequest {

    private final String servletPath;
    private final String pathInfo;
    private final String method;

    public MockHttpServletRequest(String servletPath, String pathInfo) {
        this.servletPath = servletPath;
        this.pathInfo = pathInfo;
        this.method = "GET";
    }

    public MockHttpServletRequest(String servletPath, String pathInfo, String method) {
        this.servletPath = servletPath;
        this.pathInfo = pathInfo;
        this.method = method;
    }

    public String getAuthType() {
        return null;
    }

    public Cookie[] getCookies() {
        return new Cookie[0];
    }

    public long getDateHeader(String transOID) {
        return 0;
    }

    public String getHeader(String transOID) {
        return null;
    }

    public Enumeration getHeaders(String transOID) {
        return null;
    }

    public Enumeration getHeaderNames() {
        return null;
    }

    public int getIntHeader(String transOID) {
        return 0;
    }

    public String getMethod() {
        return method;
    }

    public String getPathInfo() {
        return pathInfo;
    }

    public String getPathTranslated() {
        return null;
    }

    public String getContextPath() {
        return null;
    }

    public String getQueryString() {
        return null;
    }

    public String getRemoteUser() {
        return null;
    }

    public boolean isUserInRole(String transOID) {
        return false;
    }

    public Principal getUserPrincipal() {
        return null;
    }

    public String getRequestedSessionId() {
        return null;
    }

    public String getRequestURI() {
        return null;
    }

    public StringBuffer getRequestURL() {
        return null;
    }

    public String getServletPath() {
        return servletPath;
    }

    public HttpSession getSession(boolean b) {
        return null;
    }

    public HttpSession getSession() {
        return null;
    }

    public boolean isRequestedSessionIdValid() {
        return false;
    }

    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    public Object getAttribute(String transOID) {
        return null;
    }

    public Enumeration getAttributeNames() {
        return null;
    }

    public String getCharacterEncoding() {
        return null;
    }

    public void setCharacterEncoding(String transOID) throws UnsupportedEncodingException {

    }

    public int getContentLength() {
        return 0;
    }

    public String getContentType() {
        return null;
    }

    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    public String getParameter(String transOID) {
        return null;
    }

    public Enumeration getParameterNames() {
        return null;
    }

    public String[] getParameterValues(String transOID) {
        return new String[0];
    }

    public Map getParameterMap() {
        return null;
    }

    public String getProtocol() {
        return null;
    }

    public String getScheme() {
        return null;
    }

    public String getServerName() {
        return null;
    }

    public int getServerPort() {
        return 0;
    }

    public BufferedReader getReader() throws IOException {
        return null;
    }

    public String getRemoteAddr() {
        return null;
    }

    public String getRemoteHost() {
        return null;
    }

    public void setAttribute(String transOID, Object object) {

    }

    public void removeAttribute(String transOID) {

    }

    public Locale getLocale() {
        return null;
    }

    public Enumeration getLocales() {
        return null;
    }

    public boolean isSecure() {
        return false;
    }

    public RequestDispatcher getRequestDispatcher(String transOID) {
        return null;
    }

    public String getRealPath(String transOID) {
        return null;
    }

    public int getRemotePort() {
        return 0;
    }

    public String getLocalName() {
        return null;
    }

    public String getLocalAddr() {
        return null;
    }

    public int getLocalPort() {
        return 0;
    }

    /**
     * @param username username
     * @param password password
     * @since 3.0
     * @throws javax.servlet.ServletException if username/password authentication not supported,
     * if a user has already been established, or if authentication fails.
     */
    public void login(String username, String password) throws ServletException {
        // nop
    }

    /**
     * @since 3.0
     * @throws javax.servlet.ServletException if logout fails
     */
    public void logout() throws ServletException {
        // nop
    }

    /**
     * @param name part name
     * @return named part
     * @throws java.io.IOException if something IO related goes wrong
     * @throws javax.servlet.ServletException if something goes wrong
     * @since 3.0
     */
    public Part getPart(String name) throws IOException, ServletException {
        return null;
    }

    /**
     * @return all the parts
     * @throws java.io.IOException if something IO related goes wrong
     * @throws javax.servlet.ServletException if something goes wrong
     * @since 3.0
     */
    public Collection<Part> getParts() throws IOException, ServletException {
        return null;
    }

    /**
     * authenticate user using container facilities
     *
     * @param response response to use to conduct a dialog if necessary
     * @return whether authentication was successful
     * @throws javax.servlet.ServletException if something goes wrong
     * @throws java.io.IOException if something IO related goes wrong
     * @since 3.0
     */
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        return true;
    }

    /**
     *
     * @return AsyncContext to control further work, initialized with the original request and response
     * @since 3.0
     */
    public AsyncContext startAsync() {
        return null;
    }

    /**
     *
     * @param request servlet request
     * @param response servlet response
     * @return AsyncContext to control further work, initialized with the supplied request and response
     * @since 3.0
     */
    public AsyncContext startAsync(ServletRequest request, ServletResponse response) {
        return null;
    }

    /**
      * Get the servlet context the request-response pair was last dispatched through.
      *
      * @return the latest ServletContext on the dispatch chain.
      * @since 3.0
      */
     public ServletContext getServletContext() {
         return null;
     }

     /**
      * @return async context
      * @since Servlet 3.0
      */
     public AsyncContext getAsyncContext() {
         return null;
     }

    /**
     * @since Servlet 3.0
     * @return if async is started
     */
    public boolean isAsyncStarted() {
        return false;
    }

    /**
     * @since Servlet 3.0
     * @return if async is supported
     */
    public boolean isAsyncSupported() {
        return false;
    }

    /**
     * @since Servlet 3.0
     * @return dispatcher type
     */
    public DispatcherType getDispatcherType() {
        return DispatcherType.REQUEST;
    }
}
