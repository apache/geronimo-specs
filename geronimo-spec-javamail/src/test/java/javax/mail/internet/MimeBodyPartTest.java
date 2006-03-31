/**
 *
 * Copyright 2006 The Apache Software Foundation
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

package javax.mail.internet;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import junit.framework.TestCase;

/**
 * @version $Rev$ $Date$
 */
public class MimeBodyPartTest extends TestCase {

    public void testGetSize() throws MessagingException {
        MimeBodyPart part = new MimeBodyPart();
        assertEquals(part.getSize(), -1);

        part = new MimeBodyPart(new InternetHeaders(), new byte[] {'a', 'b', 'c'});
        assertEquals(part.getSize(), 3);
    }

    public void testGetLineCount() throws MessagingException {
        MimeBodyPart part = new MimeBodyPart();
        assertEquals(part.getLineCount(), -1);

        part = new MimeBodyPart(new InternetHeaders(), new byte[] {'a', 'b', 'c'});
        assertEquals(part.getLineCount(), -1);
    }


    public void testGetContentType() throws MessagingException {
        MimeBodyPart part = new MimeBodyPart();
        assertEquals(part.getContentType(), "text/plain");

        part.setHeader("Content-Type", "text/xml");
        assertEquals(part.getContentType(), "text/xml");

        part = new MimeBodyPart();
        part.setText("abc");
        assertEquals(part.getContentType(), "text/plain");
    }


    public void testIsMimeType() throws MessagingException {
        MimeBodyPart part = new MimeBodyPart();
        assertTrue(part.isMimeType("text/plain"));
        assertTrue(part.isMimeType("text/*"));

        part.setHeader("Content-Type", "text/xml");
        assertTrue(part.isMimeType("text/xml"));
        assertTrue(part.isMimeType("text/*"));
    }


    public void testGetDisposition() throws MessagingException {
        MimeBodyPart part = new MimeBodyPart();
        assertNull(part.getDisposition());

        part.setDisposition("inline");
        assertEquals(part.getDisposition(), "inline");
    }


    public void testSetDescription() throws MessagingException, UnsupportedEncodingException {
        MimeBodyPart part = new MimeBodyPart();

        String simpleSubject = "Yada, yada";

        String complexSubject = "Yada, yada\u0081";

        String mungedSubject = "Yada, yada\u003F";

        part.setDescription(simpleSubject);
        assertEquals(part.getDescription(), simpleSubject);

        part.setDescription(complexSubject, "UTF-8");
        assertEquals(part.getDescription(), complexSubject);
        assertEquals(part.getHeader("Content-Description", null), MimeUtility.encodeText(complexSubject, "UTF-8", null));

        part.setDescription(null);
        assertNull(part.getDescription());
    }
}

