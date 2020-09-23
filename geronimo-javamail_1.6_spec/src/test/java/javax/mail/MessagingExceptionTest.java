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
 * @version $Revision $ $Date$
 */
public class MessagingExceptionTest extends TestCase {
    private RuntimeException e;
    private MessagingException d;
    private MessagingException c;
    private MessagingException b;
    private MessagingException a;
    public MessagingExceptionTest(final String name) {
        super(name);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        //Initialize cause with null, make sure the getCause will not be affected
        a = new MessagingException("A", null);
        b = new MessagingException("B");
        c = new MessagingException("C");
        d = new MessagingException("D");
        e = new RuntimeException("E");
    }
    
    public void testMessagingExceptionString() {
        assertEquals("A", a.getMessage());
    }
    
    public void testNextException() {
        assertTrue(a.setNextException(b));
        assertEquals(b, a.getNextException());
        assertEquals(b, a.getCause());
        
        assertTrue(a.setNextException(c));
        assertEquals(b, a.getNextException());
        assertEquals(c, b.getNextException());
        assertEquals(c, b.getCause());
        
        assertTrue(a.setNextException(d));
        
        assertEquals(b, a.getNextException());
        assertEquals(b, a.getCause());
        
        assertEquals(c, b.getNextException());
        assertEquals(c, b.getCause());
        
        assertEquals(d, c.getNextException());
        assertEquals(d, c.getCause());
        
        final String message = a.getMessage();
        final int ap = message.indexOf("A");
        final int bp = message.indexOf("B");
        final int cp = message.indexOf("C");
        assertTrue("A does not contain 'A'", ap != -1);
        assertTrue("B does not contain 'B'", bp != -1);
        assertTrue("C does not contain 'C'", cp != -1);
    }
    
    public void testNextExceptionWrong() {
        assertTrue(a.setNextException(e));
        assertFalse(a.setNextException(b));
    }
    
    public void testNextExceptionWrong2() {
        assertTrue(a.setNextException(e));
        assertFalse(a.setNextException(b));
    }
    
    public void testMessagingExceptionStringException() {
        final MessagingException x = new MessagingException("X", a);
        assertEquals("X (javax.mail.MessagingException: A)", x.getMessage());
        assertEquals(a, x.getNextException());
        assertEquals(a, x.getCause());
    }
}
