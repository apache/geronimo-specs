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
public final class FromTerm extends AddressTerm {
	
	private static final long serialVersionUID = 5214730291502658665L;
	
    public FromTerm(final Address match) {
        super(match);
    }

    @Override
    public boolean match(final Message message) {
        try {
            final Address from[] = message.getFrom();
            if (from == null) {
                return false; 
            }
            for (int i = 0; i < from.length; i++) {
                if (match(from[i])) {
                    return true;
                }
            }
            return false;
        } catch (final MessagingException e) {
            return false;
        }
    }
}
