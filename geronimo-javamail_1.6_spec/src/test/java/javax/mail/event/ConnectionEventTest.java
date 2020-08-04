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

import junit.framework.TestCase;

/**
 * @version $Rev$ $Date$
 */
public class ConnectionEventTest extends TestCase {
    public static class ConnectionListenerTest implements ConnectionListener {
        private int state = 0;
        public void closed(final ConnectionEvent event) {
            if (state != 0) {
                fail("Recycled ConnectionListener");
            }
            state = ConnectionEvent.CLOSED;
        }
        public void disconnected(final ConnectionEvent event) {
            if (state != 0) {
                fail("Recycled ConnectionListener");
            }
            state = ConnectionEvent.DISCONNECTED;
        }
        public int getState() {
            return state;
        }
        public void opened(final ConnectionEvent event) {
            if (state != 0) {
                fail("Recycled ConnectionListener");
            }
            state = ConnectionEvent.OPENED;
        }
    }
    public ConnectionEventTest(final String name) {
        super(name);
    }
    private void doEventTests(final int type) {
        final ConnectionEvent event = new ConnectionEvent(this, type);
        assertEquals(this, event.getSource());
        assertEquals(type, event.getType());
        final ConnectionListenerTest listener = new ConnectionListenerTest();
        event.dispatch(listener);
        assertEquals("Unexpcted method dispatched", type, listener.getState());
    }
    public void testEvent() {
        doEventTests(ConnectionEvent.CLOSED);
        doEventTests(ConnectionEvent.OPENED);
        doEventTests(ConnectionEvent.DISCONNECTED);
    }
}
