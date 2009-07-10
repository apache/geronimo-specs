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


package javax.security.jacc;

import junit.framework.TestCase;

/**
 * @version $Rev$ $Date$
 */
public class HTTPMethodSpecTest extends TestCase {
    
    public void testHTTPMethodSpec() throws Exception {
        testHTTPMethodSpec(true);
        testHTTPMethodSpec(false);
        assertEquals("NONE", new HTTPMethodSpec("NONE", true).getActions());
    }

    public void testHTTPMethodSpec(boolean parseTransport) throws Exception {
        assertEquals("", new HTTPMethodSpec(null, parseTransport).getActions());
        assertEquals("", new HTTPMethodSpec("", parseTransport).getActions());
        assertEquals("", new HTTPMethodSpec("!", parseTransport).getActions());
        assertEquals("GET", new HTTPMethodSpec("GET", parseTransport).getActions());
        assertEquals("GET,PUT", new HTTPMethodSpec("GET,PUT", parseTransport).getActions());
        assertEquals("GET,PUT", new HTTPMethodSpec("PUT,GET", parseTransport).getActions());
        assertEquals("FOO", new HTTPMethodSpec("FOO", parseTransport).getActions());
        assertEquals("!GET", new HTTPMethodSpec("!GET", parseTransport).getActions());
        assertEquals("!FOO", new HTTPMethodSpec("!FOO", parseTransport).getActions());
        assertEquals("!GET,PUT", new HTTPMethodSpec("!PUT,GET", parseTransport).getActions());
        assertFalse(new HTTPMethodSpec("GET", parseTransport).equals(new HTTPMethodSpec("!GET", parseTransport)));
    }
}
