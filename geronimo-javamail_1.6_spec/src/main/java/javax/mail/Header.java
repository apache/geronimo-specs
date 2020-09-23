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

package javax.mail;

/**
 * Class representing a header field.
 *
 * @version $Rev$ $Date$
 */
public class Header {
    /**
     * The name of the header.
     */
    protected String name;
    /**
     * The header value (can be null).
     */
    protected String value;

    /**
     * Constructor initializing all immutable fields.
     *
     * @param name  the name of this header
     * @param value the value of this header
     */
    public Header(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Return the name of this header.
     *
     * @return the name of this header
     */
    public String getName() {
        return name;
    }

    /**
     * Return the value of this header.
     *
     * @return the value of this header
     */
    public String getValue() {
        return value;
    }
}
