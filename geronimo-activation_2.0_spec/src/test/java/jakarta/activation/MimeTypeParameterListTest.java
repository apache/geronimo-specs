/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package jakarta.activation;

import java.util.Enumeration;

import junit.framework.TestCase;

/**
 *
 * @version $Rev$ $Date$
 */
public class MimeTypeParameterListTest extends TestCase {
	public void testEmptyParameterList() {
		MimeTypeParameterList parameterList = new MimeTypeParameterList();
		assertEquals(0, parameterList.size());
        assertTrue(parameterList.isEmpty());
	}

	public void testSimpleParameterList() throws MimeTypeParseException {
		MimeTypeParameterList parameterList = new MimeTypeParameterList(";name=value");
        assertEquals(1, parameterList.size());
        assertFalse(parameterList.isEmpty());
        Enumeration e = parameterList.getNames();
        assertTrue(e.hasMoreElements());
        assertEquals("name", e.nextElement());
        assertFalse(e.hasMoreElements());
		assertEquals("value", parameterList.get("name"));
	}

    public void testQuotedValue() throws MimeTypeParseException {
		MimeTypeParameterList parameterList = new MimeTypeParameterList(";name=\"val()ue\"");
        assertEquals(1, parameterList.size());
        assertEquals("val()ue", parameterList.get("name"));
    }

	public void testWhiteSpacesParameterList() throws MimeTypeParseException {
		MimeTypeParameterList parameterList = new MimeTypeParameterList("; name= value");
        assertEquals(1, parameterList.size());
        assertEquals("name", parameterList.getNames().nextElement());
		assertEquals("value", parameterList.get("name"));
	}

	public void testLongParameterList() throws MimeTypeParseException {
		MimeTypeParameterList parameterList = new MimeTypeParameterList(";name1=value1; name2 = value2; name3=value3;name4  = value4");
		assertEquals(4, parameterList.size());
		assertEquals("value1", parameterList.get("name1"));
		assertEquals("value2", parameterList.get("name2"));
		assertEquals("value3", parameterList.get("name3"));
		assertEquals("value4", parameterList.get("name4"));
	}

    public void testCaseInsensitivity() throws MimeTypeParseException {
		MimeTypeParameterList parameterList = new MimeTypeParameterList(";name1=value; NAME2=VALUE; NaMe3=VaLuE");
        assertEquals(3, parameterList.size());
        assertEquals("value", parameterList.get("name1"));
        assertEquals("VALUE", parameterList.get("name2"));
        assertEquals("VaLuE", parameterList.get("name3"));
        assertEquals("value", parameterList.get("NAME1"));
        assertEquals("value", parameterList.get("NaMe1"));
        parameterList.remove("NAME1");
        assertNull(parameterList.get("name1"));
        parameterList.remove("name3");
        assertEquals("; name2=VALUE", parameterList.toString());
    }

	public void testNoValueParameterList() {
		try {
			MimeTypeParameterList parameterList = new MimeTypeParameterList("; name=");
            fail("Expected MimeTypeParseException");
        } catch (MimeTypeParseException e) {
            // ok
		}
	}

	public void testMissingValueParameterList() {
		try {
			MimeTypeParameterList parameterList = new MimeTypeParameterList("; name=;name2=value");
            fail("Expected MimeTypeParseException");
        } catch (MimeTypeParseException e) {
            // ok
		}
	}

	public void testNoNameParameterList() {
		try {
			MimeTypeParameterList parameterList = new MimeTypeParameterList("; = value");
// if running on Java 6, the activation framework is included directly in the JRE...it appears
// the reference implementation is not handling this correctly.
//          fail("Expected MimeTypeParseException");
        } catch (MimeTypeParseException e) {
            // ok
		}
	}

	public void testUnterminatedQuotedString() {
		try {
			MimeTypeParameterList parameterList = new MimeTypeParameterList("; = \"value");
            fail("Expected MimeTypeParseException");
        } catch (MimeTypeParseException e) {
            // ok
		}
	}

	public void testSpecialInAttribute() {
        String specials = "()<>@,;:\\\"/[]?= \t";
        for (int i=0; i < specials.length(); i++) {
            try {
				MimeTypeParameterList parameterList = new MimeTypeParameterList(";na"+specials.charAt(i)+"me=value");
                fail("Expected MimeTypeParseException for special: " + specials.charAt(i));
            } catch (MimeTypeParseException e) {
                // ok
            }
        }
	}
}

