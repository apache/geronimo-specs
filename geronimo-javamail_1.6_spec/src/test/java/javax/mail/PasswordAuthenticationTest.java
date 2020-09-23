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

import junit.framework.TestCase;

/**
 * @version $Rev$ $Date$
 */
public class PasswordAuthenticationTest extends TestCase {
    public PasswordAuthenticationTest(final String name) {
        super(name);
    }
    public void testPA() {
        final String user = String.valueOf(System.currentTimeMillis());
        final String password = "JobbyJobbyJobby" + user;
        final PasswordAuthentication pa = new PasswordAuthentication(user, password);
        assertEquals(user, pa.getUserName());
        assertEquals(password, pa.getPassword());
    }
    public void testPasswordAuthentication() {
        final PasswordAuthentication pa = new PasswordAuthentication("Alex", "xelA");
        assertEquals("Alex", pa.getUserName());
        assertEquals("xelA", pa.getPassword());
    }
}
