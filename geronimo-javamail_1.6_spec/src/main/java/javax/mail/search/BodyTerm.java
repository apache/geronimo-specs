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

package javax.mail.search;

import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

/**
 * Term that matches on a message body. All {@link javax.mail.BodyPart parts} that have
 * a MIME type of "text/*" are searched.
 *
 * @version $Rev$ $Date$
 */
public final class BodyTerm extends StringTerm {
	
	private static final long serialVersionUID = -4888862527916911385L;
	
    public BodyTerm(final String pattern) {
        super(pattern);
    }

    @Override
    public boolean match(final Message message) {
        try {
            return matchPart(message);
        } catch (final IOException e) {
            return false;
        } catch (final MessagingException e) {
            return false;
        }
    }

    private boolean matchPart(final Part part) throws MessagingException, IOException {
        if (part.isMimeType("multipart/*")) {
            final Multipart mp = (Multipart) part.getContent();
            final int count = mp.getCount();
            for (int i=0; i < count; i++) {
                final BodyPart bp = mp.getBodyPart(i);
                if (matchPart(bp)) {
                    return true;
                }
            }
            return false;
        } else if (part.isMimeType("text/*")) {
            final String content = (String) part.getContent();
            return super.match(content);
        } else if (part.isMimeType("message/rfc822")) {
            // nested messages need recursion        
            return matchPart((Part)part.getContent());
        } else {
            return false;
        }
    }
}
