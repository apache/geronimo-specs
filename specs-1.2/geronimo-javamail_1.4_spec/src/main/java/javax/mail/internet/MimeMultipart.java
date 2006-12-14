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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;

import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.MultipartDataSource;

import org.apache.geronimo.mail.util.SessionUtil;

/**
 * @version $Rev$ $Date$
 */
public class MimeMultipart extends Multipart {
	private static final String MIME_IGNORE_MISSING_BOUNDARY = "mail.mime.multipart.ignoremissingendboundary";

    /**
     * DataSource that provides our InputStream.
     */
    protected DataSource ds;
    /**
     * Indicates if the data has been parsed.
     */
    protected boolean parsed = true;

    // the content type information
    private transient ContentType type;

    // indicates if we've seen the final boundary line when parsing.
    private boolean complete = true;

    // MIME multipart preable text that can appear before the first boundary line.
    private String preamble = null;

    /**
     * Create an empty MimeMultipart with content type "multipart/mixed"
     */
    public MimeMultipart() {
        this("mixed");
    }

    /**
     * Create an empty MimeMultipart with the subtype supplied.
     *
     * @param subtype the subtype
     */
    public MimeMultipart(String subtype) {
        type = new ContentType("multipart", subtype, null);
        type.setParameter("boundary", getBoundary());
        contentType = type.toString();
    }

    /**
     * Create a MimeMultipart from the supplied DataSource.
     *
     * @param dataSource the DataSource to use
     * @throws MessagingException
     */
    public MimeMultipart(DataSource dataSource) throws MessagingException {
        ds = dataSource;
        if (dataSource instanceof MultipartDataSource) {
            super.setMultipartDataSource((MultipartDataSource) dataSource);
            parsed = true;
        } else {
            type = new ContentType(ds.getContentType());
            contentType = type.toString();
            parsed = false;
        }
    }

    public void setSubType(String subtype) throws MessagingException {
        type.setSubType(subtype);
        contentType = type.toString();
    }

    public int getCount() throws MessagingException {
        parse();
        return super.getCount();
    }

    public synchronized BodyPart getBodyPart(int part) throws MessagingException {
        parse();
        return super.getBodyPart(part);
    }

    public BodyPart getBodyPart(String cid) throws MessagingException {
        parse();
        for (int i = 0; i < parts.size(); i++) {
            MimeBodyPart bodyPart = (MimeBodyPart) parts.get(i);
            if (cid.equals(bodyPart.getContentID())) {
                return bodyPart;
            }
        }
        return null;
    }

    protected void updateHeaders() throws MessagingException {
        parse();
        for (int i = 0; i < parts.size(); i++) {
            MimeBodyPart bodyPart = (MimeBodyPart) parts.get(i);
            bodyPart.updateHeaders();
        }
    }

    private static byte[] dash = { '-', '-' };
    private static byte[] crlf = { 13, 10 };

    public void writeTo(OutputStream out) throws IOException, MessagingException {
        parse();
        String boundary = type.getParameter("boundary");
        byte[] bytes = boundary.getBytes();

        if (preamble != null) {
            byte[] preambleBytes = preamble.getBytes();
            // write this out, followed by a line break.
            out.write(preambleBytes);
            out.write(crlf);
        }

        for (int i = 0; i < parts.size(); i++) {
            BodyPart bodyPart = (BodyPart) parts.get(i);
            out.write(dash);
            out.write(bytes);
            out.write(crlf);
            bodyPart.writeTo(out);
            out.write(crlf);
        }
        out.write(dash);
        out.write(bytes);
        out.write(dash);
        out.write(crlf);
        out.flush();
    }

    protected void parse() throws MessagingException {
        if (parsed) {
            return;
        }
        try {
            ContentType cType = new ContentType(contentType);
            byte[] boundary = ("--" + cType.getParameter("boundary")).getBytes();
            InputStream is = new BufferedInputStream(ds.getInputStream());
            PushbackInputStream pushbackInStream = new PushbackInputStream(is,
                    (boundary.length + 2));
            readTillFirstBoundary(pushbackInStream, boundary);
            while (pushbackInStream.available()>0){
                MimeBodyPartInputStream partStream;
                partStream = new MimeBodyPartInputStream(pushbackInStream,
                        boundary);
                addBodyPart(new MimeBodyPart(partStream));

                // terminated by an EOF rather than a proper boundary?
                if (!partStream.boundaryFound) {
                    if (!SessionUtil.getBooleanProperty(MIME_IGNORE_MISSING_BOUNDARY, true)) {
                        throw new MessagingException("Missing Multi-part end boundary");
                    }
                    complete = false;
                }
            }
        } catch (Exception e){
            throw new MessagingException(e.toString(),e);
        }
        parsed = true;
    }

    /**
     * Move the read pointer to the begining of the first part
     * read till the end of first boundary.  Any data read before this point are
     * saved as the preamble.
     *
     * @param pushbackInStream
     * @param boundary
     * @throws MessagingException
     */
    private boolean readTillFirstBoundary(PushbackInputStream pushbackInStream, byte[] boundary) throws MessagingException {
        ByteArrayOutputStream preambleStream = new ByteArrayOutputStream();

        try {
            while (pushbackInStream.available() > 0) {
                int value = pushbackInStream.read();
                if ((byte) value == boundary[0]) {
                    int boundaryIndex = 0;
                    while (pushbackInStream.available() > 0 && (boundaryIndex < boundary.length)
                            && ((byte) value == boundary[boundaryIndex])) {
                        value = pushbackInStream.read();
                        if (value == -1)
                            throw new MessagingException(
                                    "Unexpected End of Stream while searching for first Mime Boundary");
                        boundaryIndex++;
                    }
                    if (boundaryIndex == boundary.length) { // boundary found
                        pushbackInStream.read();

                        // save the preamble, if there is one.
                        byte[] preambleBytes = preambleStream.toByteArray();
                        if (preambleBytes.length > 0) {
                            preamble = new String(preambleBytes);
                        }
                        return true;
                    }
                    else {
                        // we need to add this to the preamble.  We write the part of the boundary that
                        // actually matched, followed by the character that was the mismatch.
                        preambleStream.write(boundary, 0, boundaryIndex);
                        preambleStream.write((byte)value);
                    }
                }
                else {
                    // this is part of the preamble.
                    preambleStream.write((byte)value);
                }
            }
        } catch (IOException ioe) {
            throw new MessagingException(ioe.toString(), ioe);
        }
        return false;
    }

    protected InternetHeaders createInternetHeaders(InputStream in) throws MessagingException {
        return new InternetHeaders(in);
    }

    protected MimeBodyPart createMimeBodyPart(InternetHeaders headers, byte[] data) throws MessagingException {
        return new MimeBodyPart(headers, data);
    }

    protected MimeBodyPart createMimeBodyPart(InputStream in) throws MessagingException {
        return new MimeBodyPart(in);
    }

    // static used to track boudary value allocations to help ensure uniqueness.
    private static int part;

    private synchronized static String getBoundary() {
        int i;
        synchronized(MimeMultipart.class) {
            i = part++;
        }
        StringBuffer buf = new StringBuffer(64);
        buf.append("----=_Part_").append(i).append('_').append((new Object()).hashCode()).append('.').append(System.currentTimeMillis());
        return buf.toString();
    }

    private class MimeBodyPartInputStream extends InputStream {
        PushbackInputStream inStream;
        public boolean boundaryFound = false;
        byte[] boundary;

        public MimeBodyPartInputStream(PushbackInputStream inStream,
                                       byte[] boundary) {
            super();
            this.inStream = inStream;
            this.boundary = boundary;
        }

        public int read() throws IOException {
            if (boundaryFound) {
                return -1;
            }
            // read the next value from stream
            int value = inStream.read();
            // A problem occured because all the mime parts tends to have a /r/n at the end. Making it hard to transform them to correct DataSources.
            // This logic introduced to handle it
            //TODO look more in to this && for a better way to do this
            if (value == 13) {
                value = inStream.read();
                if (value != 10) {
                    inStream.unread(value);
                    return 13;
                } else {
                    value = inStream.read();
                    if ((byte) value != boundary[0]) {
                        inStream.unread(value);
                        inStream.unread(10);
                        return 13;
                    }
                }
            } else if ((byte) value != boundary[0]) {
                return value;
            }
            // read value is the first byte of the boundary. Start matching the
            // next characters to find a boundary
            int boundaryIndex = 0;
            while ((boundaryIndex < boundary.length)
                    && ((byte) value == boundary[boundaryIndex])) {
                value = inStream.read();
                boundaryIndex++;
            }
            if (boundaryIndex == boundary.length) { // boundary found
                boundaryFound = true;
                // read the end of line character
                if (inStream.read() == '-' && value == '-') {
                    //Last mime boundary should have a succeeding "--"
                    //as we are on it, read the terminating CRLF
                    inStream.read();
                    inStream.read();
                }
                return -1;
            }
            // Boundary not found. Restoring bytes skipped.
            // write first skipped byte, push back the rest
            if (value != -1) { // Stream might have ended
                inStream.unread(value);
            }
            inStream.unread(boundary, 1, boundaryIndex - 1);
            return boundary[0];
        }
    }

    /**
     * Return true if the final boundary line for this multipart was
     * seen when parsing the data.
     *
     * @return
     * @exception MessagingException
     */
    public boolean isComplete() throws MessagingException {
        // make sure we've parsed this
        parse();
        return complete;
    }


    /**
     * Returns the preamble text that appears before the first bady
     * part of a MIME multi part.  The preamble is optional, so this
     * might be null.
     *
     * @return The preamble text string.
     * @exception MessagingException
     */
    public String getPreamble() throws MessagingException {
        parse();
        return preamble;
    }

    /**
     * Set the message preamble text.  This will be written before
     * the first boundary of a multi-part message.
     *
     * @param preamble The new boundary text.  This is complete lines of text, including
     *                 new lines.
     *
     * @exception MessagingException
     */
    public void setPreamble(String preamble) throws MessagingException {
        this.preamble = preamble;
    }
}
