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

import java.util.Properties;

import junit.framework.TestCase;

/**
 * @version $Rev$ $Date$
 */
public class SessionTest extends TestCase {
    public void testAddProvider() throws MessagingException {
        final Properties props = System.getProperties();
         // Get a Session object
        final Session mailSession = Session.getDefaultInstance(props, null);

        mailSession.addProvider(new Provider(Provider.Type.TRANSPORT, "foo", NullTransport.class.getName(), "Apache", "Java 1.4 Test"));

        // retrieve the transport
        Transport trans = mailSession.getTransport("foo");

        assertTrue(trans instanceof NullTransport);

        mailSession.setProtocolForAddress("foo", "foo");

        trans = mailSession.getTransport(new FooAddress());

        assertTrue(trans instanceof NullTransport);
    }

    static public class NullTransport extends Transport {
        public NullTransport(final Session session, final URLName urlName) {
            super(session, urlName);
        }

        @Override
        public void sendMessage(final Message message, final Address[] addresses) throws MessagingException {
            // do nothing
        }

        @Override
        protected boolean protocolConnect(final String host, final int port, final String user, final String password) throws MessagingException {
            return true; // always connect
        }

    }

    static public class FooAddress extends Address {
        public FooAddress() {
        }

        @Override
        public String getType() {
            return "foo";
        }

        @Override
        public String toString() {
            return "yada";
        }


        @Override
        public boolean equals(final Object other) {
            return true;
        }
    }
}

