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
public class MessageChangedEventTest extends TestCase {
    public MessageChangedEventTest(final String name) {
        super(name);
    }
    public void testEvent() {
        doEventTests(MessageChangedEvent.ENVELOPE_CHANGED);
        doEventTests(MessageChangedEvent.FLAGS_CHANGED);
    }
    private void doEventTests(final int type) {
        final MessageChangedEvent event = new MessageChangedEvent(this, type, null);
        assertEquals(this, event.getSource());
        assertEquals(type, event.getMessageChangeType());
        final MessageChangedListenerTest listener = new MessageChangedListenerTest();
        event.dispatch(listener);
        assertEquals("Unexpcted method dispatched", type, listener.getState());
    }
    public static class MessageChangedListenerTest
        implements MessageChangedListener {
        private int state = 0;
        public void messageChanged(final MessageChangedEvent event) {
            if (state != 0) {
                fail("Recycled Listener");
            }
            state = event.getMessageChangeType();
        }
        public int getState() {
            return state;
        }
    }
}
