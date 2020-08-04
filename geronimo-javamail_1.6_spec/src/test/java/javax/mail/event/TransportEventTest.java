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

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.TestData;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import junit.framework.TestCase;

/**
 * @version $Rev$ $Date$
 */
public class TransportEventTest extends TestCase {
    public TransportEventTest(final String name) {
        super(name);
    }
    public void testEvent() throws AddressException {
        doEventTests(TransportEvent.MESSAGE_DELIVERED);
        doEventTests(TransportEvent.MESSAGE_PARTIALLY_DELIVERED);
        doEventTests(TransportEvent.MESSAGE_NOT_DELIVERED);
    }
    private void doEventTests(final int type) throws AddressException {
        final Folder folder = TestData.getTestFolder();
        final Message message = TestData.getMessage();
        final Transport transport = TestData.getTestTransport();
        final Address[] sent = new Address[] { new InternetAddress("alex@here.com")};
        final Address[] empty = new Address[0];
        final TransportEvent event =
            new TransportEvent(transport, type, sent, empty, empty, message);
        assertEquals(transport, event.getSource());
        assertEquals(type, event.getType());
        final TransportListenerTest listener = new TransportListenerTest();
        event.dispatch(listener);
        assertEquals("Unexpcted method dispatched", type, listener.getState());
    }
    public static class TransportListenerTest implements TransportListener {
        private int state = 0;
        public void messageDelivered(final TransportEvent event) {
            if (state != 0) {
                fail("Recycled Listener");
            }
            state = TransportEvent.MESSAGE_DELIVERED;
        }
        public void messagePartiallyDelivered(final TransportEvent event) {
            if (state != 0) {
                fail("Recycled Listener");
            }
            state = TransportEvent.MESSAGE_PARTIALLY_DELIVERED;
        }
        public void messageNotDelivered(final TransportEvent event) {
            if (state != 0) {
                fail("Recycled Listener");
            }
            state = TransportEvent.MESSAGE_NOT_DELIVERED;
        }
        public int getState() {
            return state;
        }
    }
}
