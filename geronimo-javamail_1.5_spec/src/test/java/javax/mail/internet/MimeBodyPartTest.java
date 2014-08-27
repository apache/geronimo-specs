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

import junit.framework.TestCase;

import javax.activation.DataHandler;
import javax.mail.EncodingAware;
import javax.mail.MessagingException;
import javax.mail.Part;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @version $Rev$ $Date$
 */
public class MimeBodyPartTest extends TestCase {

    File basedir = new File(System.getProperty("basedir", "."));
    File testInput = new File(basedir, "src/test/resources/test.dat");

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
        final MimeBodyPart part = new MimeBodyPart();
        assertTrue(part.isMimeType("text/plain"));
        assertTrue(part.isMimeType("text/*"));

        part.setHeader("Content-Type", "text/xml");
        assertTrue(part.isMimeType("text/xml"));
        assertTrue(part.isMimeType("text/*"));
    }


    public void testGetDisposition() throws MessagingException {
        final MimeBodyPart part = new MimeBodyPart();
        assertNull(part.getDisposition());

        part.setDisposition("inline");
        assertEquals(part.getDisposition(), "inline");
    }
    
    public void testJavaMail15AttachmentDisposition() throws MessagingException, IOException {
        final MimeBodyPart part = new MimeBodyPart();
        assertNull(part.getDisposition());
        final File testInput = new File(basedir, "src/test/resources/test.dat");
        part.attachFile(testInput);
        assertEquals(Part.ATTACHMENT, part.getDisposition());
    }
    
    public void testJavaMail15EncodingAware() throws MessagingException, IOException {
    	final File testInput = new File(basedir, "src/test/resources/test.dat");
    	final MimeBodyPart part = new MimeBodyPart();
        part.attachFile(testInput, "application/octet-stream", "7bit"); // depending on the OS encoding can change
        part.updateHeaders();
        assertTrue(part.getDataHandler().getContentType().equals("application/octet-stream"));
        assertEquals("7bit", part.getEncoding());
        
        final MimeBodyPart part2 = new MimeBodyPart();
        part2.attachFile(testInput,"application/pdf","base64");
        part2.updateHeaders();
        assertTrue(part2.getDataHandler().getContentType().equals("application/pdf"));
        assertEquals("base64", part2.getEncoding());
    }


    public void testSetDescription() throws MessagingException, UnsupportedEncodingException {
        final MimeBodyPart part = new MimeBodyPart();

        final String simpleSubject = "Yada, yada";

        final String complexSubject = "Yada, yada\u0081";

        final String mungedSubject = "Yada, yada\u003F";

        part.setDescription(simpleSubject);
        assertEquals(part.getDescription(), simpleSubject);

        part.setDescription(complexSubject, "UTF-8");
        assertEquals(part.getDescription(), complexSubject);
        assertEquals(part.getHeader("Content-Description", null), MimeUtility.encodeText(complexSubject, "UTF-8", null));

        part.setDescription(null);
        assertNull(part.getDescription());
    }

    public void testSetFileName() throws Exception {
        final MimeBodyPart part = new MimeBodyPart();
        part.setFileName("test.dat");

        assertEquals("test.dat", part.getFileName());

        final ContentDisposition disp = new ContentDisposition(part.getHeader("Content-Disposition", null));
        assertEquals("test.dat", disp.getParameter("filename"));

        final ContentType type = new ContentType(part.getHeader("Content-Type", null));
        assertEquals("test.dat", type.getParameter("name"));

        final MimeBodyPart part2 = new MimeBodyPart();

        part2.setHeader("Content-Type", type.toString());

        assertEquals("test.dat", part2.getFileName());
        part2.setHeader("Content-Type", null);
        part2.setHeader("Content-Disposition", disp.toString());
        assertEquals("test.dat", part2.getFileName());
    }


    public void testAttachments() throws Exception {
        MimeBodyPart part = new MimeBodyPart();

        final byte[] testData = getFileData(testInput);

        part.attachFile(testInput);
        assertEquals(part.getFileName(), testInput.getName());

        part.updateHeaders();

        File temp1 = File.createTempFile("MIME", ".dat");
        temp1.deleteOnExit();

        part.saveFile(temp1);

        byte[] tempData = getFileData(temp1);

        compareFileData(testData, tempData);


        ByteArrayOutputStream out = new ByteArrayOutputStream();

        part.writeTo(out);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

        MimeBodyPart part2 = new MimeBodyPart(in);

        temp1 = File.createTempFile("MIME", ".dat");
        temp1.deleteOnExit();

        part2.saveFile(temp1);

        tempData = getFileData(temp1);

        compareFileData(testData, tempData);


        part = new MimeBodyPart();

        part.attachFile(testInput.getPath());
        assertEquals(part.getFileName(), testInput.getName());

        part.updateHeaders();

        temp1 = File.createTempFile("MIME", ".dat");
        temp1.deleteOnExit();

        part.saveFile(temp1.getPath());

        tempData = getFileData(temp1);

        compareFileData(testData, tempData);

        out = new ByteArrayOutputStream();
        part.writeTo(out);

        in = new ByteArrayInputStream(out.toByteArray());

        part2 = new MimeBodyPart(in);

        temp1 = File.createTempFile("MIME", ".dat");
        temp1.deleteOnExit();

        part2.saveFile(temp1.getPath());

        tempData = getFileData(temp1);

        compareFileData(testData, tempData);
    }

    private byte[] getFileData(final File source) throws Exception {
        final FileInputStream testIn = new FileInputStream(source);

        final byte[] testData = new byte[(int)source.length()];

        testIn.read(testData);
        testIn.close();
        return testData;
    }

    private void compareFileData(final byte[] file1, final byte [] file2) {
        assertEquals(file1.length, file2.length);
        for (int i = 0; i < file1.length; i++) {
            assertEquals(file1[i], file2[i]);
        }
    }



    class TestMimeBodyPart extends MimeBodyPart {
        public TestMimeBodyPart() {
            super();
        }


        @Override
        public void updateHeaders() throws MessagingException {
            super.updateHeaders();
        }
    }
}

