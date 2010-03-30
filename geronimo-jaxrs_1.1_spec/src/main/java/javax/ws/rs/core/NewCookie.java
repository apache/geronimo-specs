/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */

package javax.ws.rs.core;

import javax.ws.rs.ext.RuntimeDelegate;
import javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate;

public class NewCookie extends Cookie {
    public static final int DEFAULT_MAX_AGE = -1;

    private final String    comment;
    private final int       maxAge;
    private final boolean   isSecure;

    public NewCookie(String name, String value) {
        this(name, value, null, null, null, DEFAULT_MAX_AGE, false);
    }

    public NewCookie(String name,
                     String value,
                     String path,
                     String domain,
                     String comment,
                     int maxAge,
                     boolean isSecure) {
        this(name, value, path, domain, DEFAULT_VERSION, comment, maxAge, isSecure);
    }

    public NewCookie(String name,
                     String value,
                     String path,
                     String domain,
                     int version,
                     String comment,
                     int maxAge,
                     boolean isSecure) {
        super(name, value, path, domain, version);
        this.comment = comment;
        this.maxAge = maxAge;
        this.isSecure = isSecure;
    }

    public NewCookie(Cookie cookie) {
        this(cookie, null, DEFAULT_MAX_AGE, false);
    }

    public NewCookie(Cookie cookie, String comment, int maxAge, boolean isSecure) {
        super(cookie.getName(), cookie.getValue(), cookie.getPath(), cookie.getDomain(), cookie
            .getVersion());
        this.comment = comment;
        this.maxAge = maxAge;
        this.isSecure = isSecure;
    }

    @Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        // note that this must be a NewCookie exactly
        if (getClass() != obj.getClass()) {
            return false;
        }

        NewCookie other = (NewCookie)obj;
        if (!getName().equals(other.getName())) {
            return false;
        }

        if (getVersion() != other.getVersion()) {
            return false;
        }

        if (isSecure != other.isSecure) {
            return false;
        }

        if (maxAge != other.maxAge) {
            return false;
        }

        String value = getValue();
        if (value == null) {
            if (other.getValue() != null) {
                return false;
            }
        } else {
            if (!value.equals(other.getValue())) {
                return false;
            }
        }

        String path = getPath();
        if (path == null) {
            if (other.getPath() != null) {
                return false;
            }
        } else {
            if (!path.equals(other.getPath())) {
                return false;
            }
        }

        String domain = getDomain();
        if (domain == null) {
            if (other.getDomain() != null) {
                return false;
            }
        } else {
            if (!domain.equals(other.getDomain())) {
                return false;
            }
        }

        if (comment == null) {
            if (other.comment != null) {
                return false;
            }
        } else {
            if (!comment.equals(other.comment)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + ((comment == null) ? 0 : comment.hashCode());
        result = 31 * result + maxAge;
        result = 31 * result + ((isSecure) ? 1 : 0);
        return result;
    }

    public String getComment() {
        return comment;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public boolean isSecure() {
        return isSecure;
    }

    public Cookie toCookie() {
        return new Cookie(getName(), getValue(), getPath(), getDomain(), getVersion());
    }

    private static final HeaderDelegate<NewCookie> headerDelegate =
                                                                      RuntimeDelegate
                                                                          .getInstance()
                                                                          .createHeaderDelegate(NewCookie.class);

    @Override
    public String toString() {
        return headerDelegate.toString(this);
    }

    public static NewCookie valueOf(String value) {
        return headerDelegate.fromString(value);
    }
}
