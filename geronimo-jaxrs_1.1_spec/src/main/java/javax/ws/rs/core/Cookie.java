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

public class Cookie {

    public static final int DEFAULT_VERSION = 1;

    private final String    name;
    private final String    value;
    private final String    path;
    private final String    domain;
    private final int       version;

    public Cookie(String name, String value) {
        this(name, value, null, null);
    }

    public Cookie(String name, String value, String path, String domain) {
        this(name, value, path, domain, DEFAULT_VERSION);
    }

    public Cookie(String name, String value, String path, String domain, int version) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.value = value;
        this.path = path;
        this.domain = domain;
        this.version = version;
    }

    @Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        // note that this must be a Cookie exactly
        if (getClass() != obj.getClass()) {
            return false;
        }

        Cookie other = (Cookie)obj;
        if (!name.equals(other.name)) {
            return false;
        }

        if (version != other.version) {
            return false;
        }

        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else {
            if (!value.equals(other.value)) {
                return false;
            }
        }

        if (path == null) {
            if (other.path != null) {
                return false;
            }
        } else {
            if (!path.equals(other.path)) {
                return false;
            }
        }

        if (domain == null) {
            if (other.domain != null) {
                return false;
            }
        } else {
            if (!domain.equals(other.domain)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + name.hashCode();
        result = 31 * result + ((value == null) ? 0 : value.hashCode());
        result = 31 * result + ((path == null) ? 0 : path.hashCode());
        result = 31 * result + ((domain == null) ? 0 : domain.hashCode());
        result = 31 * result + version;
        return result;
    }

    public String getDomain() {
        return domain;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getValue() {
        return value;
    }

    public int getVersion() {
        return version;
    }

    private static final HeaderDelegate<Cookie> headerDelegate =
                                                                   RuntimeDelegate
                                                                       .getInstance()
                                                                       .createHeaderDelegate(Cookie.class);

    @Override
    public String toString() {
        return headerDelegate.toString(this);
    }

    public static Cookie valueOf(String value) {
        return headerDelegate.fromString(value);
    }
}
