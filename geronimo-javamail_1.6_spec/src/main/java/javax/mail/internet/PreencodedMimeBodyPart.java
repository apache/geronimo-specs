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

import java.io.IOException;
import java.io.OutputStream;

import javax.mail.MessagingException;

/**
 * @version $Rev$ $Date$
 */


public class PreencodedMimeBodyPart extends MimeBodyPart {
    // the defined transfer encoding
    private final String transferEncoding;


    /**
     * Create a new body part with the specified MIME transfer encoding.
     *
     * @param encoding The content encoding.
     */
    public PreencodedMimeBodyPart(final String encoding) {
        transferEncoding = encoding;
    }


    /**
     * Retieve the defined encoding for this body part.
     *
     * @return
     * @exception MessagingException
     */
    @Override
    public String getEncoding() throws MessagingException {
        return transferEncoding;
    }

    /**
     * Write the body part content to the stream without applying
     * and additional encodings.
     *
     * @param out    The target output stream.
     *
     * @exception IOException
     * @exception MessagingException
     */
    @Override
    public void writeTo(final OutputStream out) throws IOException, MessagingException {
        headers.writeTo(out, null);
        // add the separater between the headers and the data portion.
        out.write('\r');
        out.write('\n');
        // write this out without getting an encoding stream
        getDataHandler().writeTo(out);
        out.flush();
    }


    /**
     * Override of update headers to ensure the transfer encoding
     * is forced to the correct type.
     *
     * @exception MessagingException
     */
    @Override
    protected void updateHeaders() throws MessagingException {
        super.updateHeaders();
        setHeader("Content-Transfer-Encoding", transferEncoding);
    }
}

