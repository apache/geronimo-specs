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

import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;

/**
 * @version $Rev$ $Date$
 */
public final class ReceivedDateTerm extends DateTerm {
	
	private static final long serialVersionUID = -2756695246195503170L;
	
    public ReceivedDateTerm(final int comparison, final Date date) {
        super(comparison, date);
    }

    @Override
    public boolean match(final Message message) {
        try {
            final Date date = message.getReceivedDate();
            if (date == null) {
                return false; 
            }
            
            return match(date);
        } catch (final MessagingException e) {
            return false;
        }
    }
    
    
    @Override
    public boolean equals(final Object other) {
        if (other == this) {
			return true;
		}
        if (other instanceof ReceivedDateTerm == false) {
			return false;
		}
        return super.equals(other); 
    }
}
