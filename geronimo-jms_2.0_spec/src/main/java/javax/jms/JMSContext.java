/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package javax.jms;

import java.io.Serializable;

/**
 * Created by johnament on 10/5/14.
 */
public interface JMSContext extends AutoCloseable {
    public static final int AUTO_ACKNOWLEDGE = 1;
    public static final int CLIENT_ACKNOWLEDGE = 2;
    public static final int DUPS_OK_ACKNOWLEDGE = 3;
    public static final int SESSION_TRANSACTED = 0;

    void acknowledge();

    void close();

    void commit();

    QueueBrowser createBrowser(Queue queue);

    QueueBrowser createBrowser(Queue queue, String messageSelector);

    BytesMessage createBytesMessage();

    JMSConsumer createConsumer(Destination destination);

    JMSConsumer createConsumer(Destination destination, String messageSelector);

    JMSConsumer createConsumer(Destination destination, String messageSelector, boolean noLocal);

    JMSContext createContext(int sessionMode);

    JMSConsumer createDurableConsumer(Topic topic, String name);

    JMSConsumer createDurableConsumer(Topic topic, String name, String messageSelector, boolean noLocal);

    MapMessage createMapMessage();

    Message createMessage();

    ObjectMessage createObjectMessage();

    ObjectMessage createObjectMessage(Serializable object);

    JMSProducer createProducer();

    Queue createQueue(String queueName);

    JMSConsumer createSharedConsumer(Topic topic, String sharedSubscriptionName);

    JMSConsumer createSharedConsumer(Topic topic, String sharedSubscriptionName, String messageSelector);

    JMSConsumer createSharedDurableConsumer(Topic topic, String name);

    JMSConsumer createSharedDurableConsumer(Topic topic, String name, String messageSelector);

    StreamMessage createStreamMessage();

    TemporaryQueue createTemporaryQueue();

    TemporaryTopic createTemporaryTopic();

    TextMessage createTextMessage();

    TextMessage createTextMessage(String text);

    Topic createTopic(String topicName);

    boolean getAutoStart();

    String getClientID();

    ExceptionListener getExceptionListener();

    ConnectionMetaData getMetaData();

    int getSessionMode();

    boolean getTransacted();

    void recover();

    void rollback();

    void setAutoStart(boolean autoStart);

    void setClientID(String clientID);

    void setExceptionListener(ExceptionListener listener);

    void start();

    void stop();

    void unsubscribe(String name);

}