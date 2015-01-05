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

/**
 * @version $Rev: 467553 $ $Date: 2006-10-25 00:01:51 -0400 (Wed, 25 Oct 2006) $
 */
public class QueueRequestor {
    private QueueSession session;
    private TemporaryQueue temporaryQueue;
    private QueueSender sender;
    private QueueReceiver receiver;

    public QueueRequestor(QueueSession session, Queue queue)
        throws JMSException
    {
        super();

        if(queue == null) {
            throw new InvalidDestinationException("Invalid queue");
        }
        
        setSession(session);
        setTemporaryQueue(session.createTemporaryQueue());
        setSender(session.createSender(queue));
        setReceiver(session.createReceiver(getTemporaryQueue()));
    }

    public Message request(Message message) throws JMSException {
        message.setJMSReplyTo(getTemporaryQueue());
        getSender().send(message);
        return getReceiver().receive();
    }

    public void close() throws JMSException {
        getSession().close();
        getTemporaryQueue().delete();
    }

    private void setReceiver(QueueReceiver receiver) {
        this.receiver = receiver;
    }

    private QueueReceiver getReceiver() {
        return receiver;
    }

    private void setSender(QueueSender sender) {
        this.sender = sender;
    }

    private QueueSender getSender() {
        return sender;
    }

    private void setSession(QueueSession session) {
        this.session = session;
    }

    private QueueSession getSession() {
        return session;
    }

    private void setTemporaryQueue(TemporaryQueue temporaryQueue) {
        this.temporaryQueue = temporaryQueue;
    }

    private TemporaryQueue getTemporaryQueue() {
        return temporaryQueue;
    }
}
