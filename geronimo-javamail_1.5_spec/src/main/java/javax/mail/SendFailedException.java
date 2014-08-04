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

package javax.mail;

/**
 * @version $Rev$ $Date$
 */
public class SendFailedException extends MessagingException {
	
	private static final long serialVersionUID = -6457531621682372913L;
	
    protected transient Address invalid[];
    protected transient Address validSent[];
    protected transient Address validUnsent[];

    public SendFailedException() {
        super();
    }

    public SendFailedException(final String message) {
        super(message);
    }

    public SendFailedException(final String message, final Exception cause) {
        super(message, cause);
    }

    public SendFailedException(final String message,
                               final Exception cause,
                               final Address[] validSent,
                               final Address[] validUnsent,
                               final Address[] invalid) {
        this(message, cause);
        this.invalid = invalid;
        this.validSent = validSent;
        this.validUnsent = validUnsent;
    }

    public Address[] getValidSentAddresses() {
        return validSent;
    }

    public Address[] getValidUnsentAddresses() {
        return validUnsent;
    }

    public Address[] getInvalidAddresses() {
        return invalid;
    }
}
