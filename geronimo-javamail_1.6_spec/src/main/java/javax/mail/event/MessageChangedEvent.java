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

package javax.mail.event;

import javax.mail.Message;

/**
 * @version $Rev$ $Date$
 */
public class MessageChangedEvent extends MailEvent {
	
	private static final long serialVersionUID = -4974972972105535108L;
	
    /**
     * The message's flags changed.
     */
    public static final int FLAGS_CHANGED = 1;

    /**
     * The messages envelope changed.
     */
    public static final int ENVELOPE_CHANGED = 2;

    protected transient Message msg;
    protected int type;

    /**
     * Constructor.
     *
     * @param source the folder that owns the message
     * @param type the event type
     * @param message the affected message
     */
    public MessageChangedEvent(final Object source, final int type, final Message message) {
        super(source);
        msg = message;
        this.type = type;
    }

    @Override
    public void dispatch(final Object listener) {
        final MessageChangedListener l = (MessageChangedListener) listener;
        l.messageChanged(this);
    }

    /**
     * Return the affected message.
     * @return the affected message
     */
    public Message getMessage() {
        return msg;
    }

    /**
     * Return the type of change.
     * @return the event type
     */
    public int getMessageChangeType() {
        return type;
    }
}
