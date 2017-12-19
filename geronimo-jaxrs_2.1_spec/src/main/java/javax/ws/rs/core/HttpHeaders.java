/*
 * #%L
 * Apache Geronimo JAX-RS Spec 2.0
 * %%
 * Copyright (C) 2003 - 2014 The Apache Software Foundation
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package javax.ws.rs.core;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public interface HttpHeaders {


    List<String> getRequestHeader(String name);


    String getHeaderString(String name);


    MultivaluedMap<String, String> getRequestHeaders();


    List<MediaType> getAcceptableMediaTypes();


    List<Locale> getAcceptableLanguages();


    MediaType getMediaType();


    Locale getLanguage();


    Map<String, Cookie> getCookies();


    Date getDate();


    int getLength();


    static final String ACCEPT = "Accept";

    static final String ACCEPT_CHARSET = "Accept-Charset";

    static final String ACCEPT_ENCODING = "Accept-Encoding";

    static final String ACCEPT_LANGUAGE = "Accept-Language";

    static final String ALLOW = "Allow";

    static final String AUTHORIZATION = "Authorization";

    static final String CACHE_CONTROL = "Cache-Control";

    static final String CONTENT_DISPOSITION = "Content-Disposition";

    static final String CONTENT_ENCODING = "Content-Encoding";

    static final String CONTENT_ID = "Content-ID";

    static final String CONTENT_LANGUAGE = "Content-Language";

    static final String CONTENT_LENGTH = "Content-Length";

    static final String CONTENT_LOCATION = "Content-Location";

    static final String CONTENT_TYPE = "Content-Type";

    static final String DATE = "Date";

    static final String ETAG = "ETag";

    static final String EXPIRES = "Expires";

    static final String HOST = "Host";

    static final String IF_MATCH = "If-Match";

    static final String IF_MODIFIED_SINCE = "If-Modified-Since";

    static final String IF_NONE_MATCH = "If-None-Match";

    static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";

    static final String LAST_MODIFIED = "Last-Modified";

    static final String LOCATION = "Location";

    static final String LINK = "Link";

    static final String RETRY_AFTER = "Retry-After";

    static final String USER_AGENT = "User-Agent";

    static final String VARY = "Vary";

    static final String WWW_AUTHENTICATE = "WWW-Authenticate";

    static final String COOKIE = "Cookie";

    static final String SET_COOKIE = "Set-Cookie";
}
