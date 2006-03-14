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

import java.io.IOException;

import javax.servlet.ServletResponseWrapper;

/**
 * @version $Revision$ $Date$
 */
public class HttpServletResponseWrapper extends ServletResponseWrapper
        implements HttpServletResponse {
    private HttpServletResponse response;
    public HttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
        this.response = response;
    }
    
    public void addCookie(Cookie cookie) {
        this.response.addCookie(cookie);
    }

    public void addDateHeader(String name, long date) {
        this.response.addDateHeader(name, date);
    }

    public void addHeader(String name, String value) {
        this.response.addHeader(name, value);
    }

    public void addIntHeader(String name, int value) {
        this.response.addIntHeader(name, value);
    }

    public boolean containsHeader(String name) {
        return this.response.containsHeader(name);
    }

    @SuppressWarnings("deprecation")
    public String encodeRedirectUrl(String url) {
        return this.response.encodeRedirectUrl(url);
    }

    public String encodeRedirectURL(String url) {
        return this.response.encodeRedirectURL(url);
    }

    @SuppressWarnings("deprecation")
    public String encodeUrl(String url) {
        return this.response.encodeUrl(url);
    }

    public String encodeURL(String url) {
        return this.response.encodeURL(url);
    }

    public void sendError(int sc, String msg) throws IOException {
        this.response.sendError(sc, msg);
    }

    public void sendError(int sc) throws IOException {
        this.response.sendError(sc);
    }

    public void sendRedirect(String location) throws IOException {
        this.response.sendRedirect(location);
    }

    public void setDateHeader(String name, long date) {
        this.response.setDateHeader(name, date);
    }

    public void setHeader(String name, String value) {
        this.response.setHeader(name, value);
    }

    public void setIntHeader(String name, int value) {
        this.response.setIntHeader(name, value);
    }

    public void setStatus(int sc) {
        this.response.setStatus(sc);
    }
    
    @SuppressWarnings("deprecation")
    public void setStatus(int sc, String sm) {
        this.response.setStatus(sc, sm);
    }

}
