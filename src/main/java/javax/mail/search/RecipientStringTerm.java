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

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;

/**
 * @version $Rev$ $Date$
 */
public final class RecipientStringTerm extends AddressStringTerm {
	
	private static final long serialVersionUID = -8293562089611618849L;
	
    private Message.RecipientType type;

    public RecipientStringTerm(Message.RecipientType type, String pattern) {
        super(pattern);
        this.type = type;
    }

    public Message.RecipientType getRecipientType() {
        return type;
    }

    public boolean match(Message message) {
        try {
            Address from[] = message.getRecipients(type);
            if (from == null) {
                return false; 
            }
            for (int i = 0; i < from.length; i++) {
                Address address = from[i];
                if (match(address)) {
                    return true;
                }
            }
            return false;
        } catch (MessagingException e) {
            return false;
        }
    }

    public boolean equals(Object other) {
        if (other == this) return true;
        if (other instanceof RecipientStringTerm == false) return false;
        final RecipientStringTerm otherTerm = (RecipientStringTerm) other;
        return this.pattern.equals(otherTerm.pattern) && this.type == otherTerm.type;
    }

    public int hashCode() {
        return pattern.hashCode() + type.hashCode();
    }
}
