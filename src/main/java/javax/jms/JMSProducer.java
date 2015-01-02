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
import java.util.Map;
import java.util.Set;

public interface JMSProducer {
    JMSProducer clearProperties();

    CompletionListener getAsync();

    boolean getBooleanProperty(String name);

    byte getByteProperty(String name);

    long getDeliveryDelay();

    int getDeliveryMode();

    boolean getDisableMessageID();

    boolean getDisableMessageTimestamp();

    double getDoubleProperty(String name);

    float getFloatProperty(String name);

    int getIntProperty(String name);

    String getJMSCorrelationID();

    byte[] getJMSCorrelationIDAsBytes();

    Destination getJMSReplyTo();

    String getJMSType();

    long getLongProperty(String name);

    Object getObjectProperty(String name);

    int getPriority();

    Set<String> getPropertyNames();

    short getShortProperty(String name);

    String getStringProperty(String name);

    long getTimeToLive();

    boolean propertyExists(String name);

    JMSProducer send(Destination destination, byte[] body);

    JMSProducer send(Destination destination, Map<String, Object> body);

    JMSProducer send(Destination destination, Message message);

    JMSProducer send(Destination destination, Serializable body);

    JMSProducer send(Destination destination, String body);

    JMSProducer setAsync(CompletionListener completionListener);

    JMSProducer setDeliveryDelay(long deliveryDelay);

    JMSProducer setDeliveryMode(int deliveryMode);

    JMSProducer setDisableMessageID(boolean value);

    JMSProducer setDisableMessageTimestamp(boolean value);

    JMSProducer setJMSCorrelationID(String correlationID);

    JMSProducer setJMSCorrelationIDAsBytes(byte[] correlationID);

    JMSProducer setJMSReplyTo(Destination replyTo);

    JMSProducer setJMSType(String type);

    JMSProducer setPriority(int priority);

    JMSProducer setProperty(String name, boolean value);

    JMSProducer setProperty(String name, byte value);

    JMSProducer setProperty(String name, double value);

    JMSProducer setProperty(String name, float value);

    JMSProducer setProperty(String name, int value);

    JMSProducer setProperty(String name, long value);

    JMSProducer setProperty(String name, Object value);

    JMSProducer setProperty(String name, short value);

    JMSProducer setProperty(String name, String value);

    JMSProducer setTimeToLive(long timeToLive);
}
