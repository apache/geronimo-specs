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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Session;

import junit.framework.TestCase;

import org.apache.geronimo.mail.util.Base64;

public class MimeTest extends TestCase {

    public void testWriteRead() throws Exception {
        System.setProperty("mail.mime.decodefilename", "true");

        final Session session = Session.getDefaultInstance(new Properties(), null);
        final MimeMessage mime = new MimeMessage(session);
        final MimeMultipart parts = new MimeMultipart("related; type=\"text/xml\"; start=\"<xml>\"");
        final MimeBodyPart xmlPart = new MimeBodyPart();
        xmlPart.setContentID("<xml>");
        xmlPart.setDataHandler(new DataHandler(new ByteArrayDataSource("<hello/>".getBytes(), "text/xml")));
        parts.addBodyPart(xmlPart);
        final MimeBodyPart jpegPart = new MimeBodyPart();
        jpegPart.setContentID("<jpeg>");
        final String filename = "filename";
        final String encodedFilename = "=?UTF-8?B?" + new String(Base64.encode(filename.getBytes()), "ISO8859-1") + "?=";
        jpegPart.setFileName(encodedFilename);
        jpegPart.setDataHandler(new DataHandler(new ByteArrayDataSource(new byte[] { 0, 1, 2, 3, 4, 5 }, "image/jpeg")));
        parts.addBodyPart(jpegPart);
        mime.setContent(parts);
        mime.setHeader("Content-Type", parts.getContentType());

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mime.writeTo(baos);

        final MimeMessage mime2 = new MimeMessage(session, new ByteArrayInputStream(baos.toByteArray()));
        assertTrue(mime2.getContent() instanceof MimeMultipart);
        final MimeMultipart parts2 = (MimeMultipart) mime2.getContent();
        assertEquals(mime.getContentType(), mime2.getContentType());
        assertEquals(parts.getCount(), parts2.getCount());
        assertTrue(parts2.getBodyPart(0) instanceof MimeBodyPart);
        assertTrue(parts2.getBodyPart(1) instanceof MimeBodyPart);

        final MimeBodyPart xmlPart2 = (MimeBodyPart) parts2.getBodyPart(0);
        assertEquals(xmlPart.getContentID(), xmlPart2.getContentID());
        final ByteArrayOutputStream xmlBaos = new ByteArrayOutputStream();
        copyInputStream(xmlPart.getDataHandler().getInputStream(), xmlBaos);
        final ByteArrayOutputStream xmlBaos2 = new ByteArrayOutputStream();
        copyInputStream(xmlPart2.getDataHandler().getInputStream(), xmlBaos2);
        assertEquals(xmlBaos.toString(), xmlBaos2.toString());

        final MimeBodyPart jpegPart2 = (MimeBodyPart) parts2.getBodyPart(1);
        assertEquals(jpegPart.getContentID(), jpegPart2.getContentID());
        assertEquals(jpegPart.getFileName(), jpegPart2.getDataHandler().getName());
        assertEquals(filename, jpegPart2.getDataHandler().getName());
        final ByteArrayOutputStream jpegBaos = new ByteArrayOutputStream();
        copyInputStream(jpegPart.getDataHandler().getInputStream(), jpegBaos);
        final ByteArrayOutputStream jpegBaos2 = new ByteArrayOutputStream();
        copyInputStream(jpegPart2.getDataHandler().getInputStream(), jpegBaos2);
        assertEquals(jpegBaos.toString(), jpegBaos2.toString());
    }

    public static class ByteArrayDataSource implements DataSource {
        private final byte[] data;
        private final String type;
        private String name = "unused";

        public ByteArrayDataSource(final byte[] data, final String type) {
            this.data = data;
            this.type = type;
        }

        public InputStream getInputStream() throws IOException {
            if (data == null) {
				throw new IOException("no data");
			}
            return new ByteArrayInputStream(data);
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("getOutputStream() not supported");
        }

        public String getContentType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }
    }

    public static void copyInputStream(final InputStream in, final OutputStream out) throws IOException {
        final byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) >= 0) {
            out.write(buffer, 0, len);
        }
        in.close();
        out.close();
    }
}
