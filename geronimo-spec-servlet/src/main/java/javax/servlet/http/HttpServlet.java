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
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @version $Revision$ $Date$
 */
public abstract class HttpServlet extends GenericServlet implements
        Serializable {
    // portions copied from the 2.4 impl @ geronimo
    private static final String METHOD_DELETE = "DELETE";
    private static final String METHOD_HEAD = "HEAD";
    private static final String METHOD_GET = "GET";
    private static final String METHOD_OPTIONS = "OPTIONS";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_PUT = "PUT";
    private static final String METHOD_TRACE = "TRACE";

    private static final String HEADER_IFMODSINCE = "If-Modified-Since";
    private static final String HEADER_LASTMOD = "Last-Modified";
    private static final String LOCALIZATION_FILE = "javax.servlet.http.LocalStrings";
    private static ResourceBundle localeStrings = ResourceBundle
            .getBundle(LOCALIZATION_FILE);

    public HttpServlet() {
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String msg = localeStrings.getString("http.method_get_not_supported");
        String protocol = req.getProtocol();
        if (protocol.endsWith("1.1")) {
            res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, msg);
        } else {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String msg = localeStrings.getString("http.method_get_not_supported");
        String protocol = req.getProtocol();
        if (protocol.endsWith("1.1")) {
            res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, msg);
        } else {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
        }
    }

    protected void doHead(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        NoBodyResponse response = new NoBodyResponse(res);

        doGet(req, response);
        response.setContentLength();
    }

    protected void doOptions(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Method[] methods = getAllDeclaredMethods(this.getClass());

        boolean ALLOW_GET = false;
        boolean ALLOW_HEAD = false;
        boolean ALLOW_POST = false;
        boolean ALLOW_PUT = false;
        boolean ALLOW_DELETE = false;
        boolean ALLOW_TRACE = true;
        boolean ALLOW_OPTIONS = true;

        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];

            if (m.getName().equals("doGet")) {
                ALLOW_GET = true;
                ALLOW_HEAD = true;
            }
            if (m.getName().equals("doPost"))
                ALLOW_POST = true;
            if (m.getName().equals("doPut"))
                ALLOW_PUT = true;
            if (m.getName().equals("doDelete"))
                ALLOW_DELETE = true;
        }

        String allow = null;
        if (ALLOW_GET)
            if (allow == null)
                allow = METHOD_GET;
        if (ALLOW_HEAD)
            if (allow == null)
                allow = METHOD_HEAD;
            else
                allow += ", " + METHOD_HEAD;
        if (ALLOW_POST)
            if (allow == null)
                allow = METHOD_POST;
            else
                allow += ", " + METHOD_POST;
        if (ALLOW_PUT)
            if (allow == null)
                allow = METHOD_PUT;
            else
                allow += ", " + METHOD_PUT;
        if (ALLOW_DELETE)
            if (allow == null)
                allow = METHOD_DELETE;
            else
                allow += ", " + METHOD_DELETE;
        if (ALLOW_TRACE)
            if (allow == null)
                allow = METHOD_TRACE;
            else
                allow += ", " + METHOD_TRACE;
        if (ALLOW_OPTIONS)
            if (allow == null)
                allow = METHOD_OPTIONS;
            else
                allow += ", " + METHOD_OPTIONS;

        res.setHeader("Allow", allow);
    }

    private Method[] getAllDeclaredMethods(Class c) {
        if (c.getName().equals("javax.servlet.http.HttpServlet"))
            return null;

        int j = 0;
        Method[] parentMethods = getAllDeclaredMethods(c.getSuperclass());
        Method[] thisMethods = c.getDeclaredMethods();

        if (parentMethods != null) {
            Method[] allMethods = new Method[parentMethods.length
                    + thisMethods.length];
            for (int i = 0; i < parentMethods.length; i++) {
                allMethods[i] = parentMethods[i];
                j = i;
            }
            j++;
            for (int i = j; i < thisMethods.length + j; i++) {
                allMethods[i] = thisMethods[i - j];
            }
            return allMethods;
        }
        return thisMethods;
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String msg = localeStrings.getString("http.method_get_not_supported");
        String protocol = req.getProtocol();
        if (protocol.endsWith("1.1")) {
            res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, msg);
        } else {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
        }
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String msg = localeStrings.getString("http.method_get_not_supported");
        String protocol = req.getProtocol();
        if (protocol.endsWith("1.1")) {
            res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, msg);
        } else {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
        }
    }

    protected void doTrace(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        int responseLength;

        String CRLF = "\r\n";
        String responseString = "TRACE " + req.getRequestURI() + " "
                + req.getProtocol();

        Enumeration reqHeaderEnum = req.getHeaderNames();

        while (reqHeaderEnum.hasMoreElements()) {
            String headerName = (String) reqHeaderEnum.nextElement();
            responseString += CRLF + headerName + ": "
                    + req.getHeader(headerName);
        }

        responseString += CRLF;

        responseLength = responseString.length();

        res.setContentType("message/http");
        res.setContentLength(responseLength);
        ServletOutputStream out = res.getOutputStream();
        out.print(responseString);
        out.close();
        return;
    }

    protected long getLastModified(HttpServletRequest req) {
        return -1;
    }

    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String method = req.getMethod();

        if (method.equals(METHOD_GET)) {
            long lastModified = getLastModified(req);
            if (lastModified == -1) {
                // servlet doesn't support if-modified-since, no reason
                // to go through further expensive logic
                doGet(req, resp);
            } else {
                long ifModifiedSince = req.getDateHeader(HEADER_IFMODSINCE);
                if (ifModifiedSince < (lastModified / 1000 * 1000)) {
                    // If the servlet mod time is later, call doGet()
                    // Round down to the nearest second for a proper compare
                    // A ifModifiedSince of -1 will always be less
                    maybeSetLastModified(resp, lastModified);
                    doGet(req, resp);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                }
            }

        } else if (method.equals(METHOD_HEAD)) {
            long lastModified = getLastModified(req);
            maybeSetLastModified(resp, lastModified);
            doHead(req, resp);

        } else if (method.equals(METHOD_POST)) {
            doPost(req, resp);

        } else if (method.equals(METHOD_PUT)) {
            doPut(req, resp);

        } else if (method.equals(METHOD_DELETE)) {
            doDelete(req, resp);

        } else if (method.equals(METHOD_OPTIONS)) {
            doOptions(req, resp);

        } else if (method.equals(METHOD_TRACE)) {
            doTrace(req, resp);

        } else {
            //
            // Note that this means NO servlet supports whatever
            // method was requested, anywhere on this server.
            //

            String errMsg = localeStrings
                    .getString("http.method_not_implemented");
            Object[] errArgs = new Object[1];
            errArgs[0] = method;
            errMsg = MessageFormat.format(errMsg, errArgs);

            resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, errMsg);
        }
    }

    private void maybeSetLastModified(HttpServletResponse resp,
            long lastModified) {
        if (resp.containsHeader(HEADER_LASTMOD))
            return;
        if (lastModified >= 0)
            resp.setDateHeader(HEADER_LASTMOD, lastModified);
    }

    @Override
    public void service(ServletRequest req, ServletResponse res)
            throws ServletException, IOException {
        HttpServletRequest request;
        HttpServletResponse response;

        try {
            request = (HttpServletRequest) req;
            response = (HttpServletResponse) res;
        } catch (ClassCastException e) {
            throw new ServletException("non-HTTP request or response");
        }
        service(request, response);
    }

    // this whole class copied from the 2.4 jar
    class NoBodyOutputStream extends ServletOutputStream {

        private int contentLength = 0;

        NoBodyOutputStream() {
        }

        int getContentLength() {
            return contentLength;
        }

        public void write(int b) {
            contentLength++;
        }

        public void write(byte buf[], int offset, int len) throws IOException {
            if (len >= 0) {
                contentLength += len;
            } else {
                // XXX
                // isn't this really an IllegalArgumentException?

                throw new IOException("negative length");
            }
        }
    }

    class NoBodyResponse implements HttpServletResponse {
        private HttpServletResponse resp;
        private NoBodyOutputStream noBody;
        private PrintWriter writer;
        private boolean didSetContentLength;

        NoBodyResponse(HttpServletResponse r) {
            resp = r;
            noBody = new NoBodyOutputStream();
        }

        void setContentLength() {
            if (!didSetContentLength) {
                resp.setContentLength(noBody.getContentLength());
            }
        }

        //
        // SERVLET RESPONSE interface methods
        //

        public void setContentLength(int len) {
            resp.setContentLength(len);
            didSetContentLength = true;
        }

        public void setCharacterEncoding(String charset) {
            resp.setCharacterEncoding(charset);
        }

        public void setContentType(String type) {
            resp.setContentType(type);
        }

        public String getContentType() {
            return resp.getContentType();
        }

        public ServletOutputStream getOutputStream() throws IOException {
            return noBody;
        }

        public String getCharacterEncoding() {
            return resp.getCharacterEncoding();
        }

        public PrintWriter getWriter() throws UnsupportedEncodingException {
            if (writer == null) {
                OutputStreamWriter w;

                w = new OutputStreamWriter(noBody, getCharacterEncoding());
                writer = new PrintWriter(w);
            }
            return writer;
        }

        public void setBufferSize(int size) throws IllegalStateException {
            resp.setBufferSize(size);
        }

        public int getBufferSize() {
            return resp.getBufferSize();
        }

        public void reset() throws IllegalStateException {
            resp.reset();
        }

        public void resetBuffer() throws IllegalStateException {
            resp.resetBuffer();
        }

        public boolean isCommitted() {
            return resp.isCommitted();
        }

        public void flushBuffer() throws IOException {
            resp.flushBuffer();
        }

        public void setLocale(Locale loc) {
            resp.setLocale(loc);
        }

        public Locale getLocale() {
            return resp.getLocale();
        }

        //
        // HTTP SERVLET RESPONSE interface methods
        //

        public void addCookie(Cookie cookie) {
            resp.addCookie(cookie);
        }

        public boolean containsHeader(String name) {
            return resp.containsHeader(name);
        }

        @Deprecated
        public void setStatus(int sc, String sm) {
            resp.setStatus(sc, sm);
        }

        public void setStatus(int sc) {
            resp.setStatus(sc);
        }

        public void setHeader(String name, String value) {
            resp.setHeader(name, value);
        }

        public void setIntHeader(String name, int value) {
            resp.setIntHeader(name, value);
        }

        public void setDateHeader(String name, long date) {
            resp.setDateHeader(name, date);
        }

        public void sendError(int sc, String msg) throws IOException {
            resp.sendError(sc, msg);
        }

        public void sendError(int sc) throws IOException {
            resp.sendError(sc);
        }

        public void sendRedirect(String location) throws IOException {
            resp.sendRedirect(location);
        }

        public String encodeURL(String url) {
            return resp.encodeURL(url);
        }

        public String encodeRedirectURL(String url) {
            return resp.encodeRedirectURL(url);
        }

        public void addHeader(String name, String value) {
            resp.addHeader(name, value);
        }

        public void addDateHeader(String name, long value) {
            resp.addDateHeader(name, value);
        }

        public void addIntHeader(String name, int value) {
            resp.addIntHeader(name, value);
        }

        @Deprecated
        public String encodeUrl(String url) {
            return this.encodeURL(url);
        }

        @Deprecated
        public String encodeRedirectUrl(String url) {
            return this.encodeRedirectURL(url);
        }
    }
}
