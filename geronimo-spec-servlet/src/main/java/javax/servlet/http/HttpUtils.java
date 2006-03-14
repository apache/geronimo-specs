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
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import javax.servlet.ServletInputStream;

/**
 * @version $Revision$ $Date$
 */
@Deprecated
public class HttpUtils {
    private static final String LSTRING_FILE = "javax.servlet.http.LocalStrings";
    private static ResourceBundle lStrings = ResourceBundle
            .getBundle(LSTRING_FILE);

    public HttpUtils() {
    }

    public static StringBuffer getRequestURL(HttpServletRequest req) {
        // largely copied from servlet 2.4 impl @ geronimo
        StringBuffer url = new StringBuffer();
        String scheme = req.getScheme();
        int port = req.getServerPort();
        String urlPath = req.getRequestURI();

        url.append(scheme); // http, https
        url.append("://");
        url.append(req.getServerName());
        if ((scheme.equals("http") && port != 80)
                || (scheme.equals("https") && port != 443)) {
            url.append(':');
            url.append(req.getServerPort());
        }
        url.append(urlPath);
        return url;
    }

    static public Hashtable parsePostData(int len, ServletInputStream in) {
        // largely copied from servlet 2.4 impl @ geronimo
        // TODO: should a length of 0 be an IllegalArgumentException

        if (len <= 0) {
            return new Hashtable(); // cheap hack to return an empty hash
        }

        if (in == null) {
            throw new IllegalArgumentException();
        }

        //
        // Make sure we read the entire POSTed body.
        //
        byte[] postedBytes = new byte[len];
        try {
            int offset = 0;

            do {
                int inputLen = in.read(postedBytes, offset, len - offset);
                if (inputLen <= 0) {
                    String msg = lStrings.getString("err.io.short_read");
                    throw new IllegalArgumentException(msg);
                }
                offset += inputLen;
            } while ((len - offset) > 0);

        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        // XXX we shouldn't assume that the only kind of POST body
        // is FORM data encoded using ASCII or ISO Latin/1 ... or
        // that the body should always be treated as FORM data.
        //

        try {
            String postedBody = new String(postedBytes, 0, len, "8859_1");
            return parseQueryString(postedBody);
        } catch (java.io.UnsupportedEncodingException e) {
            // XXX function should accept an encoding parameter & throw this
            // exception. Otherwise throw something expected.
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    static public Hashtable parseQueryString(String s) {
        // largely copied from servlet 2.4 impl @ geronimo
        String valArray[] = null;

        if (s == null) {
            throw new IllegalArgumentException();
        }
        Hashtable<String, String[]> ht = new Hashtable<String, String[]>();
        StringBuffer sb = new StringBuffer();
        StringTokenizer st = new StringTokenizer(s, "&");
        while (st.hasMoreTokens()) {
            String pair = (String) st.nextToken();
            int pos = pair.indexOf('=');
            if (pos == -1) {
                // XXX
                // should give more detail about the illegal argument
                throw new IllegalArgumentException();
            }
            String key = parseName(pair.substring(0, pos), sb);
            String val = parseName(pair.substring(pos + 1, pair.length()), sb);
            if (ht.containsKey(key)) {
                String oldVals[] = (String[]) ht.get(key);
                valArray = new String[oldVals.length + 1];
                for (int i = 0; i < oldVals.length; i++)
                    valArray[i] = oldVals[i];
                valArray[oldVals.length] = val;
            } else {
                valArray = new String[1];
                valArray[0] = val;
            }
            ht.put(key, valArray);
        }
        return ht;
    }

    static private String parseName(String s, StringBuffer sb) {
        sb.setLength(0);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
            case '+':
                sb.append(' ');
                break;
            case '%':
                try {
                    sb.append((char) Integer.parseInt(
                            s.substring(i + 1, i + 3), 16));
                    i += 2;
                } catch (NumberFormatException e) {
                    // XXX
                    // need to be more specific about illegal arg
                    throw new IllegalArgumentException();
                } catch (StringIndexOutOfBoundsException e) {
                    String rest = s.substring(i);
                    sb.append(rest);
                    if (rest.length() == 2)
                        i++;
                }

                break;
            default:
                sb.append(c);
                break;
            }
        }
        return sb.toString();
    }
}
