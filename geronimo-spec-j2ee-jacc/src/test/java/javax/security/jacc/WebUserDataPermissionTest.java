/**
 *
 * Copyright 2003-2004 The Apache Software Foundation
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
 */

//
// This source code implements specifications defined by the Java
// Community Process. In order to remain compliant with the specification
// DO NOT add / change / or delete method signatures!
//

package javax.security.jacc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;


/**
 * @version $Rev$ $Date$
 */
public class WebUserDataPermissionTest extends TestCase {

    /*
     * Testing WebResourcePermission(java.lang.String, java.lang.String)
     */
    public void testConstructorStringString() {

        MockHttpServletRequest request = new MockHttpServletRequest("/portal", "/services/services_jdbc/_rp_services_jdbc_row1_col1_p1_adapterDisplayName/1_TranQL0x8Generic0x8JDBC0x8Resource0x8Adapter/_rp_services_jdbc_row1_col1_p1_rarPath/1_tranql0x3tranql-connector0x310x220x3rar/_rp_services_jdbc_row1_col1_p1_mode/1_params/_rp_services_jdbc_row1_col1_p1_driverClass/1_org0x2hsqldb0x2jdbcDriver/_pm_services_jdbc_row1_col1_p1/view/_rp_services_jdbc_row1_col1_p1_dbtype/1_HSQLDB0x8embedded/_rp_services_jdbc_row1_col1_p1_urlPrototype/1_jdbc:hsqldb:{Database}/_st_services_jdbc_row1_col1_p1/normal/_ps_services_jdbc_row1_col1_p1/normal/_pid/services_jdbc_row1_col1_p1/_md_services_jdbc_row1_col1_p1/view/_rp_services_jdbc_row1_col1_p1_name/1_FFFFF");
        new WebUserDataPermission(URLPatternSpec.encodeColons(request), "GET:NONE");

        WebUserDataPermission permission = new WebUserDataPermission("/foo", "GET,POST:INTEGRAL");

        assertEquals(permission.getName(), "/foo");
        assertEquals(permission.getActions(), "GET,POST:INTEGRAL");

        permission = new WebUserDataPermission("/foo", "GET,POST,POST,GET:INTEGRAL");
        assertEquals(permission.getActions(), "GET,POST:INTEGRAL");

        // bad HTTP method
        try {
            permission = new WebUserDataPermission("/foo", "GET,POST,BAR:INTEGRAL");
            fail("Bad HTTP method");
        } catch (IllegalArgumentException iae) {
        }

        // If you have a colon, then you must have a transportType
        try {
            permission = new WebUserDataPermission("/foo", "GET,POST,BAR:");
            fail("Missing transportType");
        } catch (IllegalArgumentException iae) {
        }
    }

    public void testSerialization() throws Exception {
        testSerialization(new WebUserDataPermission("/foo", "GET,POST:INTEGRAL"));
        testSerialization(new WebUserDataPermission("/foo", "GET,POST:NONE"));
        testSerialization(new WebUserDataPermission("/foo", ""));
        testSerialization(new WebUserDataPermission("/foo", ":NONE"));
        testSerialization(new WebUserDataPermission("/foo", "GET,POST"));
    }

    private void testSerialization(WebUserDataPermission permission) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(permission);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object o = ois.readObject();
        assertEquals(permission, o);
    }

    public void testImpliesStringString() {
        // An actions string without a transportType is a shorthand for a 
        // actions string with the value "NONE" as its TransportType
        WebUserDataPermission permissionFooGP = new WebUserDataPermission("/foo", "GET,POST:INTEGRAL");
        WebUserDataPermission permissionFooE = new WebUserDataPermission("/foo", "");
        WebUserDataPermission permissionFooGPN = new WebUserDataPermission("/foo", "GET,POST");

        assertTrue(permissionFooE.implies(permissionFooGP));
        assertTrue(permissionFooE.implies(permissionFooGPN));
        assertFalse(permissionFooGP.implies(permissionFooE));
        assertFalse(permissionFooGPN.implies(permissionFooE));

        assertTrue(permissionFooGPN.implies(permissionFooGP));
        assertFalse(permissionFooGP.implies(permissionFooGPN));
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
}

