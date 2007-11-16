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

//
// This source code implements specifications defined by the Java
// Community Process. In order to remain compliant with the specification
// DO NOT add / change / or delete method signatures!
//

package javax.security.jacc;

import java.security.Permission;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import junit.framework.TestCase;

/**
 * @version $Rev$ $Date$
 */
public class WebResourcePermissionTest extends TestCase {

    public void testSerialization() throws Exception {
        WebResourcePermission permission = new WebResourcePermission("/bar/*:/bar/stool", "GET,POST");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(permission);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object o = ois.readObject();
        assertEquals(permission, o);
    }

    /*
     * Testing WebResourcePermission(java.lang.String, java.lang.String)
     */
    public void testConstructorStringString() {
        // null URLPatternSpec for a WebResourcePermission
        try {
            new WebResourcePermission(null, "GET,POST");
            fail("null URLPatternSpec for a WebResourcePermission");
        } catch (IllegalArgumentException iae) {
        }


        //Default pattern
        checkPermission(new WebResourcePermission("/", "GET,POST"), "GET,POST");
        checkPermission(new WebResourcePermission("/:/foo", "GET,POST"), "GET,POST");
        checkPermission(new WebResourcePermission("/:*.asp", "GET,POST"), "GET,POST");
        checkPermission(new WebResourcePermission("/:/foo:*.asp", "GET,POST"), "GET,POST");
        checkPermission(new WebResourcePermission("", "GET,POST"), "GET,POST");
        checkPermission(new WebResourcePermission("/*", "GET,POST"), "GET,POST");
        checkPermission(new WebResourcePermission("/*:/bar/stool", "GET,POST"), "GET,POST");
        //default pattern as qualifier
        try {
            new WebResourcePermission("/bar/*:/*", "GET,POST");
            fail("/*:/");
        } catch (IllegalArgumentException iae) {
        }
        try {
            new WebResourcePermission("/bar/*:/*", "GET,POST");
            fail("/*:/*");
        } catch (IllegalArgumentException iae) {
        }
        try {
            new WebResourcePermission("/bar/*:/*", "GET,POST");
            fail("/:/");
        } catch (IllegalArgumentException iae) {
        }
        try {
            new WebResourcePermission("/bar/*:/*", "GET,POST");
            fail("/:/*");
        } catch (IllegalArgumentException iae) {
        }

        //Exact pattern
        checkPermission(new WebResourcePermission("/foo", "GET,POST"), "GET,POST");
        // missing qualifiers
        try {
            new WebResourcePermission("/foo:", "GET,POST");
            fail("/foo:");
        } catch (IllegalArgumentException iae) {
        }

        // qualifer provided when first pattern is exact
        try {
            new WebResourcePermission("/foo:/foo/bar", "GET,POST");
            fail("/foo:/foo/bar");
        } catch (IllegalArgumentException iae) {
        }
        //default pattern as a qualifier
        try {
            new WebResourcePermission("/foo:/", "GET,POST");
            fail("/foo:/");
        } catch (IllegalArgumentException iae) {
        }


        //Path prefix pattern
        checkPermission(new WebResourcePermission("/bar/*", "GET,POST"), "GET,POST");
        checkPermission(new WebResourcePermission("/bar/*:/bar/stool", "GET,POST"), "GET,POST");
        try {
            new WebResourcePermission("/foo/*:*.asp", "GET,POST");
            fail("/foo/*:*.asp");
        } catch (IllegalArgumentException iae) {
        }
        //first pattern doesn't match qualifier
        try {
            new WebResourcePermission("/bar/*:/cat/stool/*", "GET,POST");
            fail("/bar/*:/cat/stool/*");
        } catch (IllegalArgumentException iae) {
        }
        try {
            new WebResourcePermission("/bar/stool/*:/bar", "GET,POST");
            fail("/bar/stool/*:/bar");
        } catch (IllegalArgumentException iae) {
        }
        try {
            new WebResourcePermission("/bar/stool/*:/bar/*", "GET,POST");
            fail("/bar/stool/*:/bar/stool/*");
        } catch (IllegalArgumentException iae) {
        }
        //qualifier is same as first pattern
        try {
            new WebResourcePermission("/bar/stool/*:/bar/stool/*", "GET,POST");
            fail("/bar/stool/*:/bar/stool/*");
        } catch (IllegalArgumentException iae) {
        }

        //default pattern as qualifier
        try {
            new WebResourcePermission("/bar/*:/*", "GET,POST");
            fail("/bar/*:/");
        } catch (IllegalArgumentException iae) {
        }


        //Extension pattern
        checkPermission(new WebResourcePermission("*.do", "GET,POST"), "GET,POST");
        checkPermission(new WebResourcePermission("*.do:/login.do", "GET,POST"), "GET,POST");
        checkPermission(new WebResourcePermission("*.do:/foo/*", "GET,POST"), "GET,POST");

        //default pattern as qualifier
        try {
            new WebResourcePermission("*.do:/*", "GET,POST");
            fail("*.do:/*");
        } catch (IllegalArgumentException iae) {
        }
        //qualifier is extension pattern
        try {
            new WebResourcePermission("*.do:*.jsp", "GET,POST");
            fail("*.do:/*");
        } catch (IllegalArgumentException iae) {
        }
        //qualifier is exact and does not match first pattern
        try {
            new WebResourcePermission("*.do:/login", "GET,POST");
            fail("*.do:/*");
        } catch (IllegalArgumentException iae) {
        }
        
        //HTTP method
        checkPermission(new WebResourcePermission("/foo", "GET,POST,POST,GET"), "GET,POST");
        checkPermission(new WebResourcePermission("/foo", "GET,POST,BAR"), "GET,POST,BAR");
        try {
            new WebResourcePermission("/foo", "GET,POST,B A R");
            fail("Bad HTTP method");
        } catch (IllegalArgumentException iae) {
        }

        // bad HTTP method for a WebResourcePermission
        try {
            new WebResourcePermission("/foo", "GET,POST:INTEGRAL");
            fail("integrity constraint in a WebResourcePermission accepted");
        } catch (IllegalArgumentException iae) {
        }





    }

    private void checkPermission(Permission permission, String actions) {
        assertTrue(permission.equals(permission));
        assertEquals(actions, permission.getActions());
    }

    public void testExcluded() {
        WebResourcePermission permission = new WebResourcePermission("/foo", "!GET,POST");

        assertTrue(permission.equals(permission));
        assertEquals(permission.getName(), "/foo");
        assertEquals(permission.getActions(), "!GET,POST");

        permission = new WebResourcePermission("/foo", "!GET,POST,POST,GET");
        assertEquals(permission.getActions(), "!GET,POST");

        permission = new WebResourcePermission("/foo", "!GET,POST,BAR");
        // bad HTTP method
        try {
            permission = new WebResourcePermission("/foo", "!GET,POST,B A R");
            fail("Bad HTTP method");
        } catch (IllegalArgumentException iae) {
        }

        // bad HTTP method for a WebResourcePermission
        try {
            permission = new WebResourcePermission("/foo", "!GET,POST:INTEGRAL");
        } catch (IllegalArgumentException iae) {
        }

        // null URLPatternSpec for a WebResourcePermission
        try {
            permission = new WebResourcePermission(null, "!GET,POST");
            fail("null URLPatternSpec for a WebResourcePermission");
        } catch (IllegalArgumentException iae) {
        }


    }

    public void testImpliesStringString() {

        // The argument is an instanceof WebResourcePermission 
        Permission pA = new WebResourcePermission("/foo", "");
        Permission pB = new WebUserDataPermission("/foo", "");

        assertFalse(pA.implies(pB));
        assertFalse(pB.implies(pA));

        pA = new WebResourcePermission("/foo", "");
        pB = new WebResourcePermission("/foo", "GET,POST");

        assertTrue(pA.implies(pB));
        assertFalse(pB.implies(pA));

        pA = new WebResourcePermission("/foo/*:/foo/bar", "");
        pB = new WebResourcePermission("/foo/bar", "");

        assertFalse(pA.implies(pB));
        assertFalse(pB.implies(pA));

        pA = new WebResourcePermission("/foo/bar/*:/foo/bar/cat/dog", "");
        pB = new WebResourcePermission("/foo/bar/*:/foo/bar/cat/*", "");

        assertTrue(pA.implies(pB));
        assertFalse(pB.implies(pA));

        pA = new WebResourcePermission("/:/a.jsp:/b.jsp:/c.jsp", "GET,POST,PUT,DELETE,HEAD,OPTIONS,TRACE");
        pB = new WebResourcePermission("/:/a.jsp:/c.jsp:/b.jsp", (String) null);

//        assertTrue(pA.implies(pB));  // no longer true with extension methods
        assertTrue(pB.implies(pA));
    }

    public void testImpliesExtensionExcludes() {
        //test against all permissions
        WebResourcePermission pA = new WebResourcePermission("/foo", "FOO,BAR,fizzle");
        WebResourcePermission pB = new WebResourcePermission("/foo", (String) null);
        assertFalse(pA.implies(pB));
        assertTrue(pB.implies(pA));
        assertTrue(pA.implies(pA));
        assertTrue(pB.implies(pB));

        pA = new WebResourcePermission("/foo", "!FOO,BAR,fizzle");
        pB = new WebResourcePermission("/foo", (String) null);
        assertFalse(pA.implies(pB));
        assertTrue(pB.implies(pA));
        assertTrue(pA.implies(pA));

        pA = new WebResourcePermission("/foo", "GET,POST");
        pB = new WebResourcePermission("/foo", (String) null);
        assertFalse(pA.implies(pB));
        assertTrue(pB.implies(pA));

        pA = new WebResourcePermission("/foo", "!GET,POST");
        pB = new WebResourcePermission("/foo", (String) null);
        assertFalse(pA.implies(pB));
        assertTrue(pB.implies(pA));

        //both positive sets
        pA = new WebResourcePermission("/foo", "GET,POST");
        pB = new WebResourcePermission("/foo", "GET,POST,OPTIONS");
        assertFalse(pA.implies(pB));
        assertTrue(pB.implies(pA));

        pA = new WebResourcePermission("/foo", "GET,POST");
        pB = new WebResourcePermission("/foo", "GET,POST,FOO");
        assertFalse(pA.implies(pB));
        assertTrue(pB.implies(pA));

        pA = new WebResourcePermission("/foo", "GET,FOO");
        pB = new WebResourcePermission("/foo", "GET,BAR,FOO");
        assertFalse(pA.implies(pB));
        assertTrue(pB.implies(pA));

        pA = new WebResourcePermission("/foo", "FOO,BAR");
        pB = new WebResourcePermission("/foo", "FOO,BAR,fizzle");
        assertFalse(pA.implies(pB));
        assertTrue(pB.implies(pA));

        //both exclusions
        pA = new WebResourcePermission("/foo", "!FOO,BAR,fizzle");
        pB = new WebResourcePermission("/foo", "!FOO,BAR");
        assertFalse(pA.implies(pB));
        assertTrue(pB.implies(pA));

        pA = new WebResourcePermission("/foo", "!GET,POST,FOO");
        pB = new WebResourcePermission("/foo", "!GET,POST");
        assertFalse(pA.implies(pB));
        assertTrue(pB.implies(pA));

        pA = new WebResourcePermission("/foo", "!GET,BAR,FOO");
        pB = new WebResourcePermission("/foo", "!GET,BAR");
        assertFalse(pA.implies(pB));
        assertTrue(pB.implies(pA));

        pA = new WebResourcePermission("/foo", "!GET,POST,OPTIONS");
        pB = new WebResourcePermission("/foo", "!GET,POST");
        assertFalse(pA.implies(pB));
        assertTrue(pB.implies(pA));

        //one of each
        pA = new WebResourcePermission("/foo", "GET");
        pB = new WebResourcePermission("/foo", "!FOO,BAR");
        assertFalse(pA.implies(pB));
        assertTrue(pB.implies(pA));

        pA = new WebResourcePermission("/foo", "fizzle");
        pB = new WebResourcePermission("/foo", "!FOO,BAR");
        assertFalse(pA.implies(pB));
        assertTrue(pB.implies(pA));

        pA = new WebResourcePermission("/foo", "GET");
        pB = new WebResourcePermission("/foo", "!POST");
        assertFalse(pA.implies(pB));
        assertTrue(pB.implies(pA));

        pA = new WebResourcePermission("/foo", "GET");
        pB = new WebResourcePermission("/foo", "!POST,BAR");
        assertFalse(pA.implies(pB));
        assertTrue(pB.implies(pA));

    }

    /*
     * Testing WebResourcePermission(String, String[])
     */
    public void testConstructorStringStringArray() {
    }

    public void testImpliesStringStringArray() {
    }

    /*
     * Testing WebResourcePermission(HttpServletRequest)
     */
    public void testConstructorHttpServletRequest() {
    }

    public void testImpliesHttpServletRequest() {
    }

    public void testGetActions() {
        WebResourcePermission p = new WebResourcePermission("/foo", "");
        assertEquals(p.getActions(), "");
        p = new WebResourcePermission("/foo", "!GET,POST");
        assertEquals(p.getActions(), "!GET,POST");
        p = new WebResourcePermission("/foo", "!POST,GET");
        assertEquals(p.getActions(), "!GET,POST");
        p = new WebResourcePermission("/foo", "!POST,GET,GET,POST");
        assertEquals(p.getActions(), "!GET,POST");

        //extension methods follow regular methods
        p = new WebResourcePermission("/foo", "FOO,BAR,POST,FOO,GET,GET,POST");
        assertEquals("GET,POST,BAR,FOO", p.getActions());

        p = new WebResourcePermission("/foo", "!FOO,BAR,POST,FOO,GET,GET,POST");
        assertEquals("!GET,POST,BAR,FOO", p.getActions());

    }

    public static void main(String[] args) {
        WebResourcePermissionTest test = new WebResourcePermissionTest();
        test.testConstructorStringString();
        test.testImpliesStringString();
        test.testConstructorStringStringArray();
        test.testImpliesStringStringArray();
        test.testConstructorHttpServletRequest();
        test.testImpliesHttpServletRequest();
    }
}

