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

package javax.servlet.http;

import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletException;

import java.util.Enumeration;
import java.util.Collection;
import java.io.IOException;

/**
 * Provides a convenient implementation of the HttpServletRequest interface that
 * can be subclassed by developers wishing to adapt the request to a Servlet.
 * This class implements the Wrapper or Decorator pattern. Methods default to
 * calling through to the wrapped request object.
 *
 * @version $Rev$ $Date$
 * @since v 2.3
 * @see javax.servlet.http.HttpServletRequest
 */


public class HttpServletRequestWrapper extends ServletRequestWrapper implements HttpServletRequest {

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request request to wrap
     * @throws java.lang.IllegalArgumentException
     *          if the request is null
     */
    public HttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    private HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) super.getRequest();
    }

    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        return getHttpServletRequest().authenticate(response);
    }

    /**
     * The default behavior of this method is to return changeSessionId() on the wrapped request object.
     * @return the new session id
     * @since Servlet 3.1
     */
    @Override
    public String changeSessionId() {
        return getHttpServletRequest().changeSessionId();
    }

    /**
     * The default behavior of this method is to return getAuthType()
     * on the wrapped request object.
     */
    public String getAuthType() {
        return getHttpServletRequest().getAuthType();
    }

    /**
     * The default behavior of this method is to return getCookies()
     * on the wrapped request object.
     */
    public Cookie[] getCookies() {
        return getHttpServletRequest().getCookies();
    }

    /**
     * The default behavior of this method is to return getDateHeader(String name)
     * on the wrapped request object.
     */
    public long getDateHeader(String name) {
        return getHttpServletRequest().getDateHeader(name);
    }

    /**
     * The default behavior of this method is to return getHeader(String name)
     * on the wrapped request object.
     */
    public String getHeader(String name) {
        return getHttpServletRequest().getHeader(name);
    }

    /**
     * The default behavior of this method is to return getHeaders(String name)
     * on the wrapped request object.
     */
    public Enumeration<String> getHeaders(String name) {
        return getHttpServletRequest().getHeaders(name);
    }

    /**
     * The default behavior of this method is to return getHeaderNames()
     * on the wrapped request object.
     */

    public Enumeration<String> getHeaderNames() {
        return getHttpServletRequest().getHeaderNames();
    }

    /**
     * The default behavior of this method is to return getIntHeader(String name)
     * on the wrapped request object.
     */

    public int getIntHeader(String name) {
        return getHttpServletRequest().getIntHeader(name);
    }

    /**
     * The default behavior of this method is to return getMethod()
     * on the wrapped request object.
     */
    public String getMethod() {
        return getHttpServletRequest().getMethod();
    }

    public Part getPart(String name) throws IOException, ServletException {
        return getHttpServletRequest().getPart(name);
    }

    public Collection<Part> getParts() throws IOException, ServletException {
        return getHttpServletRequest().getParts();
    }

    /**
     * The default behavior of this method is to return getPathInfo()
     * on the wrapped request object.
     */
    public String getPathInfo() {
        return getHttpServletRequest().getPathInfo();
    }

    /**
     * The default behavior of this method is to return getPathTranslated()
     * on the wrapped request object.
     */

    public String getPathTranslated() {
        return getHttpServletRequest().getPathTranslated();
    }

    /**
     * The default behavior of this method is to return getContextPath()
     * on the wrapped request object.
     */
    public String getContextPath() {
        return getHttpServletRequest().getContextPath();
    }

    /**
     * The default behavior of this method is to return getQueryString()
     * on the wrapped request object.
     */
    public String getQueryString() {
        return getHttpServletRequest().getQueryString();
    }

    /**
     * The default behavior of this method is to return getRemoteUser()
     * on the wrapped request object.
     */
    public String getRemoteUser() {
        return getHttpServletRequest().getRemoteUser();
    }

    /**
     * The default behavior of this method is to return isUserInRole(String role)
     * on the wrapped request object.
     */
    public boolean isUserInRole(String role) {
        return getHttpServletRequest().isUserInRole(role);
    }

    public void login(String username, String password) throws ServletException {
        getHttpServletRequest().login(username, password);
    }

    public void logout() throws ServletException {
        getHttpServletRequest().logout();
    }

    /**
     * The default behavior of this method is to return getUserPrincipal()
     * on the wrapped request object.
     */
    public java.security.Principal getUserPrincipal() {
        return getHttpServletRequest().getUserPrincipal();
    }

    /**
     * The default behavior of this method is to return getRequestedSessionId()
     * on the wrapped request object.
     */
    public String getRequestedSessionId() {
        return getHttpServletRequest().getRequestedSessionId();
    }

    /**
     * The default behavior of this method is to return getRequestURI()
     * on the wrapped request object.
     */
    public String getRequestURI() {
        return getHttpServletRequest().getRequestURI();
    }

    /**
     * The default behavior of this method is to return getRequestURL()
     * on the wrapped request object.
     */
    public StringBuffer getRequestURL() {
        return getHttpServletRequest().getRequestURL();
    }

    /**
     * The default behavior of this method is to return getServletPath()
     * on the wrapped request object.
     */
    public String getServletPath() {
        return getHttpServletRequest().getServletPath();
    }

    /**
     * The default behavior of this method is to return getSession(boolean create)
     * on the wrapped request object.
     */
    public HttpSession getSession(boolean create) {
        return getHttpServletRequest().getSession(create);
    }

    /**
     * The default behavior of this method is to return getSession()
     * on the wrapped request object.
     */
    public HttpSession getSession() {
        return getHttpServletRequest().getSession();
    }

    /**
     * The default behavior of this method is to return isRequestedSessionIdValid()
     * on the wrapped request object.
     */

    public boolean isRequestedSessionIdValid() {
        return getHttpServletRequest().isRequestedSessionIdValid();
    }

    /**
     * The default behavior of this method is to return isRequestedSessionIdFromCookie()
     * on the wrapped request object.
     */
    public boolean isRequestedSessionIdFromCookie() {
        return getHttpServletRequest().isRequestedSessionIdFromCookie();
    }

    /**
     * The default behavior of this method is to return isRequestedSessionIdFromURL()
     * on the wrapped request object.
     */
    public boolean isRequestedSessionIdFromURL() {
        return getHttpServletRequest().isRequestedSessionIdFromURL();
    }

    /**
     * The default behavior of this method is to return isRequestedSessionIdFromUrl()
     * on the wrapped request object.
     */
    public boolean isRequestedSessionIdFromUrl() {
        return getHttpServletRequest().isRequestedSessionIdFromUrl();
    }

    /**
     * Create an instance of HttpUpgradeHandler for an given class and uses it for the http protocol upgrade processing.
     * @param handlerClass - The HttpUpgradeHandler class used for the upgrade.
     * @return an instance of the HttpUpgradeHandler
     * @throws IOException - if an I/O error occurred during the upgrade
     * @throws ServletException - if the given handlerClass fails to be instantiated
     * @since Servlet 3.1
     * @see HttpUpgradeHandler, WebConnection
     */
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
        return getHttpServletRequest().upgrade(handlerClass);
    }
}
