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

//
// This source code implements specifications defined by the Java
// Community Process. In order to remain compliant with the specification
// DO NOT add / change / or delete method signatures!
//

package javax.jms;

import com.mockobjects.jms.MockMessage;
import com.mockobjects.jms.MockQueue;
import com.mockobjects.jms.MockQueueReceiver;
import com.mockobjects.jms.MockQueueSender;
import com.mockobjects.jms.MockQueueSession;
import com.mockobjects.jms.MockTemporaryQueue;
import com.mockobjects.jms.MockTextMessage;

import junit.framework.TestCase;

/**
 * @version $Rev: 467553 $ $Date: 2006-10-25 00:01:51 -0400 (Wed, 25 Oct 2006) $
 */
public class QueueRequestorTest extends TestCase {
    public void testConstructorNullQueue() {
        MockQueue queue = new MockQueue();

        try {
            new QueueRequestor(null, queue);
            fail();
        } catch (JMSException ex) {
            fail("JMSException should not have been thrown.");
        } catch (NullPointerException ex) {
            // success.
        }

        queue.verify();
    }

    public void testConstructorSessionNull() {
        MockQueueSession session = new MockQueueSession();

        try {
            new QueueRequestor(session, null);
            fail();
        } catch (JMSException ex) {
            // success
        }

        session.verify();
    }

    public void testConstructorSessionQueue() {
        MockQueue queue = new MockQueue();
        MockQueueReceiver receiver = new MockQueueReceiver();
        MockQueueSender sender = new MockQueueSender();
        MockQueueSession session = new MockQueueSession();
        MockTemporaryQueue tempQueue = new MockTemporaryQueue();

        session.setupReceiver(receiver);
        session.setupSender(sender);
        session.setupTemporaryQueue(tempQueue);

        try {
            new QueueRequestor(session, queue);
            // success
        } catch (JMSException ex) {
            fail();
        }

        queue.verify();
        receiver.verify();
        sender.verify();
        session.verify();
        tempQueue.verify();
    }

    public void testRequestNull() {
        MockQueue queue = new MockQueue();
        MockQueueReceiver receiver = new MockQueueReceiver();
        MockQueueSender sender = new MockQueueSender();
        MockQueueSession session = new MockQueueSession();
        MockTemporaryQueue tempQueue = new MockTemporaryQueue();

        session.setupReceiver(receiver);
        session.setupSender(sender);
        session.setupTemporaryQueue(tempQueue);

        try {
            QueueRequestor requestor = new QueueRequestor(session, queue);
            requestor.request(null);
            fail();
        } catch (JMSException ex) {
            fail("JMSException should not have been thrown.");
        } catch (NullPointerException ex) {
            // success
        }

        queue.verify();
        receiver.verify();
        sender.verify();
        session.verify();
        tempQueue.verify();
    }

    public void testRequestMessage() {
        MockMessage reply = new MockTextMessage();
        MockMessage request = new MockTextMessage();
        MockQueue queue = new MockQueue();
        MockQueueReceiver receiver = new MockQueueReceiver();
        MockQueueSender sender = new MockQueueSender();
        MockQueueSession session = new MockQueueSession();
        MockTemporaryQueue tempQueue = new MockTemporaryQueue();

        request.setExpectedJMSReplyTo(tempQueue);

        receiver.setExpectedReceiveCalls(1);
        receiver.setupReceivedMessage(reply);

        sender.setExpectedSendCalls(1);

        session.setupReceiver(receiver);
        session.setupSender(sender);
        session.setupTemporaryQueue(tempQueue);

        try {
            QueueRequestor requestor = new QueueRequestor(session, queue);
            Message jmsReply = requestor.request(request);
            assertEquals(jmsReply, reply);
        } catch (JMSException ex) {
            fail("JMSException should not have been thrown.");
        }

        reply.verify();
        request.verify();
        queue.verify();
        receiver.verify();
        sender.verify();
        session.verify();
        tempQueue.verify();
    }

    public void testClose() {
        MockQueue queue = new MockQueue();
        MockQueueReceiver receiver = new MockQueueReceiver();
        MockQueueSender sender = new MockQueueSender();
        MockQueueSession session = new MockQueueSession();
        MockTemporaryQueue tempQueue = new MockTemporaryQueue();

        session.setExpectedCloseCalls(1);
        session.setupReceiver(receiver);
        session.setupSender(sender);
        session.setupTemporaryQueue(tempQueue);

        tempQueue.setExpectedDeleteCalls(1);

        try {
            QueueRequestor requestor = new QueueRequestor(session, queue);
            requestor.close();
        } catch (JMSException ex) {
            fail();
        }

        queue.verify();
        receiver.verify();
        sender.verify();
        session.verify();
        tempQueue.verify();
    }
}
