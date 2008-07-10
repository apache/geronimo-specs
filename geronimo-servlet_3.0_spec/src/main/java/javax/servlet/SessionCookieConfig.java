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


package javax.servlet;

/**
 * @version $Rev:$ $Date:$
 * @since 3.0
 */
public class SessionCookieConfig {

    private final String domain;
    private final String path;
    private final String comment;
    private final boolean httpOnly;
    private final boolean secure;

    public SessionCookieConfig(String domain, String path, String comment, boolean httpOnly, boolean secure) {
        this.domain = domain;
        this.path = path;
        this.comment = comment;
        this.httpOnly = httpOnly;
        this.secure = secure;
    }

    public String getDomain() {
        return domain;
    }

    public String getPath() {
        return path;
    }

    public String getComment() {
        return comment;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public boolean isSecure() {
        return secure;
    }
}
