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

import java.text.MessageFormat;

/**
 * @version $Revision$ $Date$
 */
public class Cookie implements Cloneable {
    private String name;
    private String value;
    private String comment;
    private String domain;
    private int maxAge;
    private String path;
    private boolean secure;
    private int version;
    // Note -- disabled for now to allow full Netscape compatibility
    // from RFC 2068, token special case characters
    //
    // private static final String tspecials = "()<>@,;:\\\"/[]?={} \t";
    // copied from the 2.4 impl @ geronimo - is this still needed?
    // Can we go back to the full list of specials?

    private static final String tspecials = ",; ";

    public Cookie(String name, String value) {
        // copied from the 2.4 spec impl @ geronimo
        if (!isToken(name) || name.equalsIgnoreCase("Comment") // rfc2019
                || name.equalsIgnoreCase("Discard") // 2019++
                || name.equalsIgnoreCase("Domain")
                || name.equalsIgnoreCase("Expires") // (old cookies)
                || name.equalsIgnoreCase("Max-Age") // rfc2019
                || name.equalsIgnoreCase("Path")
                || name.equalsIgnoreCase("Secure")
                || name.equalsIgnoreCase("Version") || name.startsWith("$")) {
            String errMsg = "Cookie name \"{0}\" is a reserved token";
            Object[] errArgs = new Object[1];
            errArgs[0] = name;
            errMsg = MessageFormat.format(errMsg, errArgs);
            throw new IllegalArgumentException(errMsg);
        }

        this.name = name;
        this.value = value;
    }

    private boolean isToken(String value) {
        int len = value.length();

        for (int i = 0; i < len; i++) {
            char c = value.charAt(i);

            if (c < 0x20 || c >= 0x7f || tspecials.indexOf(c) != -1)
                return false;
        }
        return true;
    }

    public Object clone() {
        return new Cookie(name, value);
    }

    public String getComment() {
        return comment;
    }

    public String getDomain() {
        return domain;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public boolean getSecure() {
        return secure;
    }

    public String getValue() {
        return value;
    }

    public int getVersion() {
        return version;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDomain(String domain) {
        // copied from the 2.4 impl @ geronimo
        this.domain = domain.toLowerCase(); // IE allegedly needs this
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
