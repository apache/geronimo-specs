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

import javax.mail.Message;

/**
 * @version $Rev$ $Date$
 */
public final class MessageNumberTerm extends IntegerComparisonTerm {
	
	private static final long serialVersionUID = -5379625829658623812L;
	
    public MessageNumberTerm(final int number) {
        super(EQ, number);
    }

    @Override
    public boolean match(final Message message) {
        return match(message.getMessageNumber());
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof MessageNumberTerm)) {
            return false; 
        }
        return super.equals(other);
    }
}
