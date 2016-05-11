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

package javax.mail.internet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

/**
 * @version $Rev$ $Date$
 */
public class ContentTypeTest extends TestCase {
    public ContentTypeTest(final String arg0) {
        super(arg0);
    }
    public void testContentType() throws ParseException {
        final ContentType type = new ContentType();
        assertNull(type.getPrimaryType());
        assertNull(type.getSubType());
        assertNull(type.getParameter("charset"));
    }

    public void testContentTypeStringStringParameterList() throws ParseException {
        ContentType type;
        final ParameterList list = new ParameterList(";charset=us-ascii");
        type = new ContentType("text", "plain", list);
        assertEquals("text", type.getPrimaryType());
        assertEquals("plain", type.getSubType());
        assertEquals("text/plain", type.getBaseType());
        final ParameterList parameterList = type.getParameterList();
        assertEquals("us-ascii", parameterList.get("charset"));
        assertEquals("us-ascii", type.getParameter("charset"));

    }

    public void testContentTypeString() throws ParseException {
        ContentType type;
        type = new ContentType("text/plain");
        assertEquals("text", type.getPrimaryType());
        assertEquals("plain", type.getSubType());
        assertEquals("text/plain", type.getBaseType());
        assertNotNull(type.getParameterList());
        assertNull(type.getParameter("charset"));
        type = new ContentType("image/audio;charset=us-ascii");
        final ParameterList parameterList = type.getParameterList();
        assertEquals("image", type.getPrimaryType());
        assertEquals("audio", type.getSubType());
        assertEquals("image/audio", type.getBaseType());
        assertEquals("us-ascii", parameterList.get("charset"));
        assertEquals("us-ascii", type.getParameter("charset"));
    }
    public void testGetPrimaryType() throws ParseException {
    }
    public void testGetSubType() throws ParseException {
    }
    public void testGetBaseType() throws ParseException {
    }
    public void testGetParameter() throws ParseException {
    }
    public void testGetParameterList() throws ParseException {
    }
    public void testSetPrimaryType() throws ParseException {
        final ContentType type = new ContentType("text/plain");
        type.setPrimaryType("binary");
        assertEquals("binary", type.getPrimaryType());
        assertEquals("plain", type.getSubType());
        assertEquals("binary/plain", type.getBaseType());
    }
    public void testSetSubType() throws ParseException {
        final ContentType type = new ContentType("text/plain");
        type.setSubType("html");
        assertEquals("text", type.getPrimaryType());
        assertEquals("html", type.getSubType());
        assertEquals("text/html", type.getBaseType());
    }
    public void testSetParameter() throws ParseException {
    }
    public void testSetParameterList() throws ParseException {
    }
    public void testToString() throws ParseException {
        final ContentType type = new ContentType("text/plain");
        assertEquals("text/plain", type.toString());
        type.setParameter("foo", "bar");
        assertEquals("text/plain; foo=bar", type.toString());
        type.setParameter("bar", "me@apache.org");

        String[] tokens = type.toString().split(";");
        assertEquals(3, tokens.length);

        Set<String> parameters = new HashSet<String>();
        for (String s : tokens) {
            parameters.add(s.replaceAll("\\s+",""));
        }
        assertTrue(parameters.contains("text/plain"));
        assertTrue(parameters.contains("foo=bar"));
        assertTrue(parameters.contains("bar=\"me@apache.org\""));
    }
    public void testMatchContentType() throws ParseException {
        final ContentType type = new ContentType("text/plain");

        ContentType test = new ContentType("text/plain");

        assertTrue(type.match(test));

        test = new ContentType("TEXT/plain");
        assertTrue(type.match(test));
        assertTrue(test.match(type));

        test = new ContentType("text/PLAIN");
        assertTrue(type.match(test));
        assertTrue(test.match(type));

        test = new ContentType("text/*");
        assertTrue(type.match(test));
        assertTrue(test.match(type));

        test = new ContentType("text/xml");
        assertFalse(type.match(test));
        assertFalse(test.match(type));

        test = new ContentType("binary/plain");
        assertFalse(type.match(test));
        assertFalse(test.match(type));

        test = new ContentType("*/plain");
        assertFalse(type.match(test));
        assertFalse(test.match(type));
    }
    public void testMatchString() throws ParseException {
        final ContentType type = new ContentType("text/plain");
        assertTrue(type.match("text/plain"));
        assertTrue(type.match("TEXT/plain"));
        assertTrue(type.match("text/PLAIN"));
        assertTrue(type.match("TEXT/PLAIN"));
        assertTrue(type.match("TEXT/*"));

        assertFalse(type.match("text/xml"));
        assertFalse(type.match("binary/plain"));
        assertFalse(type.match("*/plain"));
        assertFalse(type.match(""));
        assertFalse(type.match("text/plain/yada"));
    }
    
    public void testSOAP12ContentType() throws ParseException {
        final ContentType type = new ContentType("multipart/related; type=\"application/xop+xml\"; start=\"<rootpart@soapui.org>\"; start-info=\"application/soap+xml; action=\\\"urn:upload\\\"\"; boundary=\"----=_Part_10_5804917.1223557742343\"");
        assertEquals("multipart/related", type.getBaseType());
        assertEquals("application/xop+xml", type.getParameter("type"));
        assertEquals("<rootpart@soapui.org>", type.getParameter("start"));
        assertEquals("application/soap+xml; action=\"urn:upload\"", type.getParameter("start-info"));
        assertEquals("----=_Part_10_5804917.1223557742343", type.getParameter("boundary"));
    }

}
