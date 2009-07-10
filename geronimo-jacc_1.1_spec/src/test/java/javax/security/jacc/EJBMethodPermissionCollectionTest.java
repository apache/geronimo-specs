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

import junit.framework.TestCase;

import java.security.PermissionCollection;
import java.security.Permission;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Enumeration;


/**
 * @version $Rev$ $Date$
 */
public class EJBMethodPermissionCollectionTest extends TestCase {

    public void testWildCards() {
        PermissionCollection collection = new EJBMethodPermission("HelloWorld", "").newPermissionCollection();
        collection.add(new EJBMethodPermission("HelloWorld", ""));

        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", ",,a,b,c")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", ",,")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", ",Local")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", ",Local,a,b,c")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", ",Local,")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,,a,b,c")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,,")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local,a,b,c")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local,")));
        assertFalse(collection.implies(new EJBMethodPermission("GoodbyeWorld", "")));

        collection = new EJBMethodPermission("HelloWorld", "").newPermissionCollection();
        collection.add(new EJBMethodPermission("HelloWorld", ",,a,b,c"));

        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", ",,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", ",Local,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local,")));
        assertFalse(collection.implies(new EJBMethodPermission("GoodbyeWorld", ",,a,b,c")));

        collection = new EJBMethodPermission("HelloWorld", "").newPermissionCollection();
        collection.add(new EJBMethodPermission("HelloWorld", ",,"));

        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",,a,b,c")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", ",,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local,a,b,c")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", ",Local,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,,a,b,c")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local,a,b,c")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local,")));
        assertFalse(collection.implies(new EJBMethodPermission("GoodbyeWorld", ",,")));

        collection = new EJBMethodPermission("HelloWorld", "").newPermissionCollection();
        collection.add(new EJBMethodPermission("HelloWorld", ",Local"));

        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",,")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", ",Local")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", ",Local,a,b,c")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", ",Local,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,,")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local,a,b,c")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local,")));
        assertFalse(collection.implies(new EJBMethodPermission("GoodbyeWorld", ",Local")));

        collection = new EJBMethodPermission("HelloWorld", "").newPermissionCollection();
        collection.add(new EJBMethodPermission("HelloWorld", ",Local,a,b,c"));

        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", ",Local,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local,")));
        assertFalse(collection.implies(new EJBMethodPermission("GoodbyeWorld", ",Local,a,b,c")));

        collection = new EJBMethodPermission("HelloWorld", "").newPermissionCollection();
        collection.add(new EJBMethodPermission("HelloWorld", ",Local,"));

        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local,a,b,c")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", ",Local,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local,a,b,c")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local,")));
        assertFalse(collection.implies(new EJBMethodPermission("GoodbyeWorld", ",Local,")));

        collection = new EJBMethodPermission("HelloWorld", "").newPermissionCollection();
        collection.add(new EJBMethodPermission("HelloWorld", "hello"));

        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local,")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,,a,b,c")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,,")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local,a,b,c")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local,")));
        assertFalse(collection.implies(new EJBMethodPermission("GoodbyeWorld", "hello")));

        collection = new EJBMethodPermission("HelloWorld", "").newPermissionCollection();
        collection.add(new EJBMethodPermission("HelloWorld", "hello,,a,b,c"));

        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local,")));
        assertFalse(collection.implies(new EJBMethodPermission("GoodbyeWorld", "hello,,a,b,c")));

        collection = new EJBMethodPermission("HelloWorld", "").newPermissionCollection();
        collection.add(new EJBMethodPermission("HelloWorld", "hello,,"));

        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,,a,b,c")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local,a,b,c")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local,")));
        assertFalse(collection.implies(new EJBMethodPermission("GoodbyeWorld", "hello,,")));

        collection = new EJBMethodPermission("HelloWorld", "").newPermissionCollection();
        collection.add(new EJBMethodPermission("HelloWorld", "hello,Local"));

        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,,")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local,a,b,c")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local,")));
        assertFalse(collection.implies(new EJBMethodPermission("GoodbyeWorld", "hello,Local")));

        collection = new EJBMethodPermission("HelloWorld", "").newPermissionCollection();
        collection.add(new EJBMethodPermission("HelloWorld", "hello,Local,a,b,c"));

        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local,")));
        assertFalse(collection.implies(new EJBMethodPermission("GoodbyeWorld", "hello,Local,a,b,c")));

        collection = new EJBMethodPermission("HelloWorld", "").newPermissionCollection();
        collection.add(new EJBMethodPermission("HelloWorld", "hello,Local,"));

        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", ",Local,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,,a,b,c")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,,")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local")));
        assertFalse(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local,a,b,c")));
        assertTrue(collection.implies(new EJBMethodPermission("HelloWorld", "hello,Local,")));
        assertFalse(collection.implies(new EJBMethodPermission("GoodbyeWorld", "hello,Local,")));

    }
    
    public void testSerialization() throws Exception {
        EJBMethodPermission p = new EJBMethodPermission("HelloWorld", "");
        PermissionCollection collection = p.newPermissionCollection();
        collection.add(new EJBMethodPermission("HelloWorld", ""));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(collection);
        oos.flush();
        byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        PermissionCollection collection2 = (PermissionCollection) ois.readObject();
        Enumeration <Permission> ps = collection2.elements();
        Permission p2 = ps.nextElement();
        assertEquals(p2, p);
        assertFalse(ps.hasMoreElements());
        assertTrue(collection2.implies(p));
    }
}
