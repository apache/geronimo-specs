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

import java.util.Arrays; 

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
            // We keep the original, provided content type string so that we 
            // don't end up changing quoting/formatting of the header unless 
            // changes are made to the content type.  James is somewhat dependent 
            // on that behavior. 
            contentType = ds.getContentType(); 
            type = new ContentType(contentType);
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
            InputStream is = new BufferedInputStream(ds.getInputStream());
            PushbackInputStream pushbackInStream = null;
            String boundaryString = cType.getParameter("boundary"); 
            byte[] boundary = null; 
            if (boundaryString == null) {
                pushbackInStream = new PushbackInputStream(is, 128);  
                // read until we find something that looks like a boundary string 
                boundary = readTillFirstBoundary(pushbackInStream); 
            }
            else {
                boundary = ("--" + boundaryString).getBytes();
                pushbackInStream = new PushbackInputStream(is, (boundary.length + 2));
                readTillFirstBoundary(pushbackInStream, boundary);
            }
            
            while (pushbackInStream.available() > 0){
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
    private byte[] readTillFirstBoundary(PushbackInputStream pushbackInStream) throws MessagingException {
        ByteArrayOutputStream preambleStream = new ByteArrayOutputStream();

        try {
            while (pushbackInStream.available() > 0) {
                // read the next line 
                byte[] line = readLine(pushbackInStream); 
                // if this looks like a boundary, then make it so 
                if (line.length > 2 && line[0] == '-' && line[1] == '-') {
                    // save the preamble, if there is one.
                    byte[] preambleBytes = preambleStream.toByteArray();
                    if (preambleBytes.length > 0) {
                        preamble = new String(preambleBytes);
                    }
                    return line;        
                }
                else {
                    // this is part of the preamble.
                    preambleStream.write(line);
                    preambleStream.write('\r'); 
                    preambleStream.write('\n'); 
                }
            }
            throw new MessagingException("Unexpected End of Stream while searching for first Mime Boundary");
        } catch (IOException ioe) {
            throw new MessagingException(ioe.toString(), ioe);
        }
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
    private void readTillFirstBoundary(PushbackInputStream pushbackInStream, byte[] boundary) throws MessagingException {
        ByteArrayOutputStream preambleStream = new ByteArrayOutputStream();

        try {
            while (pushbackInStream.available() > 0) {
                // read the next line 
                byte[] line = readLine(pushbackInStream); 
                
                // if this is the same length as our target boundary, then compare the two. 
                if (Arrays.equals(line, boundary)) {
                    // save the preamble, if there is one.
                    byte[] preambleBytes = preambleStream.toByteArray();
                    if (preambleBytes.length > 0) {
                        preamble = new String(preambleBytes);
                    }
                    return;        
                }
                else {
                    // this is part of the preamble.
                    preambleStream.write(line);
                    preambleStream.write('\r'); 
                    preambleStream.write('\n'); 
                }
            }
            throw new MessagingException("Unexpected End of Stream while searching for first Mime Boundary");
        } catch (IOException ioe) {
            throw new MessagingException(ioe.toString(), ioe);
        }
    }
    
    /**
     * Read a single line of data from the input stream, 
     * returning it as an array of bytes. 
     * 
     * @param in     The source input stream.
     * 
     * @return A byte array containing the line data.  Returns 
     *         null if there's nothing left in the stream.
     * @exception MessagingException
     */
    private byte[] readLine(PushbackInputStream in) throws IOException 
    {
        ByteArrayOutputStream line = new ByteArrayOutputStream();
        
        while (in.available() > 0) {
            int value = in.read(); 
            if (value == -1) {
                // if we have nothing in the accumulator, signal an EOF back 
                if (line.size() == 0) {
                    return null; 
                }
                break; 
            }
            else if (value == '\r') {
                value = in.read(); 
                // we expect to find a linefeed after the carriage return, but 
                // some things play loose with the rules. 
                if (value != '\n') {
                    in.unread(value); 
                }
                break; 
            }
            else if (value == '\n') {
                // naked linefeed, allow that 
                break; 
            }
            else {
                // write this to the line 
                line.write((byte)value); 
            }
        }
        // return this as an array of bytes 
        return line.toByteArray(); 
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

        public MimeBodyPartInputStream(PushbackInputStream inStream, byte[] boundary) {
            super();
            this.inStream = inStream;
            this.boundary = boundary;
        }

        /**
         * The base reading method for reading one character 
         * at a time. 
         * 
         * @return The read character, or -1 if an EOF was encountered. 
         * @exception IOException
         */
        public int read() throws IOException {
            if (boundaryFound) {
                return -1;
            }
            
            // read the next value from stream
            int value = inStream.read();
            // premature end?  Handle it like a boundary located 
            if (value == -1) {
                boundaryFound = true; 
                return -1; 
            }
            
            // we first need to look for a line boundary.  If we find a boundary, it can be followed by the 
            // boundary marker, so we need to remember what sort of thing we found, then read ahead looking 
            // for the part boundary. 
            
            // NB:, we only handle [\r]\n--boundary marker[--]
            // we need to at least accept what most mail servers would consider an 
            // invalid format using just '\n'
            if (value != '\r' && value != '\n') {
                // not a \r, just return the byte as is 
                return value;
            }
            
            int lineendStyle = 2;    // this indicates the type of linend we need to push back. 
            // if this is a '\r', then we require the '\n'
            if (value == '\r') {
                // now scan ahead for the second character 
                value = inStream.read();
                if (value != '\n') {
                    // only a \r, so this can't be a boundary.  Return the 
                    // \r as if it was data 
                    inStream.unread(value);
                    return '\r';
                } 
            } else {
                lineendStyle = 1;    // single character linend 
            }
            value = inStream.read();
            // if the next character is not a boundary start, we 
            // need to handle this as a normal line end 
            if ((byte) value != boundary[0]) {
                inStream.unread(value);
                // just a naked line feed...return that without pushing anything back 
                if (lineendStyle == 1) {
                    return '\n'; 
                }
                else 
                {
                    inStream.unread('\n');
                    // the next character read will by the 0x0a, which will 
                    // be handled as 
                    return '\r';
                }
            }
            
            // we're here because we found a "\r\n-" sequence, which is a potential 
            // boundary marker.  Read the individual characters of the next line until 
            // we have a mismatch 
            
            // read value is the first byte of the boundary. Start matching the
            // next characters to find a boundary
            int boundaryIndex = 0;
            while ((boundaryIndex < boundary.length) && ((byte) value == boundary[boundaryIndex])) {
                value = inStream.read();
                boundaryIndex++;
            }
            // if we didn't match all the way, we need to push back what we've read and 
            // return the EOL character 
            if (boundaryIndex != boundary.length) { 
                // Boundary not found. Restoring bytes skipped.
                // write first skipped byte, push back the rest
                
                // Stream might have ended 
                if (value != -1) { 
                    inStream.unread(value);
                }
                // restore the portion of the boundary string that we matched 
                inStream.unread(boundary, 0, boundaryIndex);
                // just a naked line feed...return that without pushing anything back 
                if (lineendStyle == 1) {
                    return '\n'; 
                }
                else 
                {
                    inStream.unread('\n');
                    // the next character read will by the 0x0a, which will 
                    // be handled as 
                    return '\r';
                }
            }
            
            // The full boundary sequence should be \r\n--boundary string[--]\r\n
            // if the last character we read was a '-', check for the end terminator 
            if (value == '-') {
                value = inStream.read();
                // crud, we have a bad boundary terminator.  We need to unwind this all the way 
                // back to the lineend and pretend none of this ever happened
                if (value != '-') {
                    // push back the end markers 
                    // Stream might have ended 
                    if (value != -1) { 
                        inStream.unread(value);
                    }
                    inStream.unread('-'); 
                    // the entire boundary string 
                    inStream.unread(boundary);
                    // just a naked line feed...return that without pushing anything back 
                    if (lineendStyle == 1) {
                        return '\n'; 
                    }
                    else 
                    {
                        inStream.unread('\n');
                        // the next character read will by the 0x0a, which will 
                        // be handled as 
                        return '\r';
                    }
                }
                // on the home stretch, but we need to verify the EOL sequence 
                value = inStream.read();
                // this must be a CR or a LF...which leaves us even more to push back and forget 
                if (value != '\r' && value != '\n') {
                    // Stream might have ended 
                    if (value != -1) { 
                        inStream.unread(value);
                    }
                    inStream.unread(value); 
                    inStream.unread('-'); 
                    inStream.unread('-'); 
                    // the entire boundary string 
                    inStream.unread(boundary);
                    // just a naked line feed...return that without pushing anything back 
                    if (lineendStyle == 1) {
                        return '\n'; 
                    }
                    else 
                    {
                        inStream.unread('\n');
                        // the next character read will by the 0x0a, which will 
                        // be handled as 
                        return '\r';
                    }
                }
                
                // if this is carriage return, check for a linefeed  
                if (value == '\r') {
                    // last check, this must be a line feed 
                    value = inStream.read();
                    if (value != '\n') {
                        // SO CLOSE!
                        // push back the end markers 
                        // Stream might have ended 
                        if (value != -1) { 
                            inStream.unread(value);
                        }
                        inStream.unread('\r'); 
                        inStream.unread('-'); 
                        inStream.unread('-'); 
                        // the entire boundary string 
                        inStream.unread(boundary);
                        // just a naked line feed...return that without pushing anything back 
                        if (lineendStyle == 1) {
                            return '\n'; 
                        }
                        else 
                        {
                            inStream.unread('\n');
                            // the next character read will by the 0x0a, which will 
                            // be handled as 
                            return '\r';
                        }
                    }
                }
            }
            else {
                // now check for a linend sequence...either \r\n or \n is accepted. 
                if (value != '\r' && value != '\n') {
                    // push back the end markers 
                    // Stream might have ended 
                    if (value != -1) { 
                        inStream.unread(value);
                    }
                    // the entire boundary string 
                    inStream.unread(boundary);
                    // just a naked line feed...return that without pushing anything back 
                    if (lineendStyle == 1) {
                        return '\n'; 
                    }
                    else 
                    {
                        inStream.unread('\n');
                        // the next character read will by the 0x0a, which will 
                        // be handled as 
                        return '\r';
                    }
                }
                
                // if this is carriage return, check for a linefeed  
                if (value == '\r') {
                    // last check, this must be a line feed 
                    value = inStream.read();
                    if (value != '\n') {
                        // SO CLOSE!
                        // push back the end markers 
                        // Stream might have ended 
                        if (value != -1) { 
                            inStream.unread(value);
                        }
                        inStream.unread('\r'); 
                        inStream.unread('-'); 
                        inStream.unread('-'); 
                        // the entire boundary string 
                        inStream.unread(boundary);
                        // just a naked line feed...return that without pushing anything back 
                        if (lineendStyle == 1) {
                            return '\n'; 
                        }
                        else 
                        {
                            inStream.unread('\n');
                            // the next character read will by the 0x0a, which will 
                            // be handled as 
                            return '\r';
                        }
                    }
                }
            }
            // we have a boundary, so return this as an EOF condition 
            boundaryFound = true;
            return -1;
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
