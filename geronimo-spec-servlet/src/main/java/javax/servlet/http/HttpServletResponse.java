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
import javax.servlet.ServletResponse;

/**
 * @version $Revision$ $Date$
 */
public interface HttpServletResponse extends ServletResponse {
    public static final int SC_ACCEPTED = 202;
    public static final int SC_BAD_GATEWAY = 502;
    public static final int SC_BAD_REQUEST = 400;
    public static final int SC_CONFLICT = 409;
    public static final int SC_CONTINUE = 100;
    public static final int SC_CREATED = 201;
    public static final int SC_EXPECTATION_FAILED = 417;
    public static final int SC_FORBIDDEN = 403;
    public static final int SC_FOUND = 302;
    public static final int SC_GATEWAY_TIMEOUT = 504;
    public static final int SC_GONE = 410;
    public static final int SC_HTTP_VERSION_NOT_SUPPORTED = 505;
    public static final int SC_INTERNAL_SERVER_ERROR = 500;
    public static final int SC_LENGTH_REQUIRED = 411;
    public static final int SC_METHOD_NOT_ALLOWED = 405;
    public static final int SC_MOVED_PERMANENTLY = 301;
    public static final int SC_MOVED_TEMPORARILY = 302;
    public static final int SC_MULTIPLE_CHOICES = 300;
    public static final int SC_NO_CONTENT = 204;
    public static final int SC_NON_AUTHORITATIVE_INFORMATION = 203;
    public static final int SC_NOT_ACCEPTABLE = 406;
    public static final int SC_NOT_FOUND = 404;
    public static final int SC_NOT_IMPLEMENTED = 501;
    public static final int SC_NOT_MODIFIED = 304;
    public static final int SC_OK = 200;
    public static final int SC_PARTIAL_CONTENT = 206;
    public static final int SC_PAYMENT_REQUIRED = 402;
    public static final int SC_PRECONDITION_FAILED = 412;
    public static final int SC_PROXY_AUTHENTICATION_REQUIRED = 407;
    public static final int SC_REQUEST_ENTITY_TOO_LARGE = 413;
    public static final int SC_REQUEST_TIMEOUT = 408;
    public static final int SC_REQUEST_URI_TOO_LONG = 414;
    public static final int SC_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    public static final int SC_RESET_CONTENT = 205;
    public static final int SC_SEE_OTHER = 303;
    public static final int SC_SERVICE_UNAVAILABLE = 503;
    public static final int SC_SWITCHING_PROTOCOLS = 101;
    public static final int SC_TEMPORARY_REDIRECT = 307;
    public static final int SC_UNAUTHORIZED = 401;
    public static final int SC_UNSUPPORTED_MEDIA_TYPE = 415;
    public static final int SC_USE_PROXY = 305;

    public void addCookie(Cookie cookie);

    public void addDateHeader(String name, long date);

    public void addHeader(String name, String value);

    public void addIntHeader(String name, int value);

    public boolean containsHeader(String name);

    @Deprecated
    public String encodeRedirectUrl(String url);

    public String encodeRedirectURL(String url);

    @Deprecated
    public String encodeUrl(String url);

    public String encodeURL(String url);

    public void sendError(int errorCode) throws IOException;

    public void sendError(int errorCode, String msg) throws IOException;

    public void sendRedirect(String location) throws IOException;

    public void setDateHeader(String name, long date);

    public void setHeader(String name, String value);

    public void setIntHeader(String name, int value);

    public void setStatus(int statusCode);

    @Deprecated
    public void setStatus(int statusCode, String statusMessage);

}
