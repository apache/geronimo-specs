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

/**
 * @version $Rev$ $Date$
 */
public class ConnectionEvent extends MailEvent {
	
	private static final long serialVersionUID = -1855480171284792957L;
	
    /**
     * A connection was opened.
     */
    public static final int OPENED = 1;

    /**
     * A connection was disconnected.
     */
    public static final int DISCONNECTED = 2;

    /**
     * A connection was closed.
     */
    public static final int CLOSED = 3;

    protected int type;

    public ConnectionEvent(final Object source, final int type) {
        super(source);
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public void dispatch(final Object listener) {
        // assume that it is the right listener type
        final ConnectionListener l = (ConnectionListener) listener;
        switch (type) {
        case OPENED:
            l.opened(this);
            break;
        case DISCONNECTED:
            l.disconnected(this);
            break;
        case CLOSED:
            l.closed(this);
            break;
        default:
            throw new IllegalArgumentException("Invalid type " + type);
        }
    }
}
