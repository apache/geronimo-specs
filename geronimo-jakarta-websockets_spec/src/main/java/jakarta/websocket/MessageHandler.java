package jakarta.websocket;

/**
 * Developers implement MessageHandlers in order to receive incoming messages during a web socket conversation. Each web
 * socket session uses no more than one thread at a time to call its MessageHandlers. This means that, provided each
 * message handler instance is used to handle messages for one web socket session, at most one thread at a time can be
 * calling any of its methods. Developers who wish to handle messages from multiple clients within the same message
 * handlers may do so by adding the same instance as a handler on each of the Session objects for the clients. In that
 * case, they will need to code with the possibility of their MessageHandler being called concurrently by multiple
 * threads, each one arising from a different client session.
 * <p/>
 * See Endpoint for a usage example.
 */
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

public interface MessageHandler {

    /**
     * This kind of handler is notified by the implementation as it becomes ready to deliver parts of a whole message.
     * <p/>
     * For handling parts of text messages, the type T is String
     * <p/>
     * For handling parts of binary messages, the allowable types for T are
     * <ul>
     * <li>ByteBuffer</li>
     * <li>byte[]</li>
     * </ul>
     * <p/>
     * Developers should not continue to reference message objects of type ByteBuffer after the completion of the
     * onMessage() call, since they may be recycled by the implementation.
     * <p/>
     * Note: Implementations may choose their own schemes for delivering large messages in smaller parts through this
     * API. These schemes may or may not bear a relationship to the underlying websocket dataframes in which the message
     * is received off the wire.
     * 
     * @param <T>
     *            The type of the object that represent pieces of the incoming message that this MessageHandler will
     *            consume.
     */
    public static interface Partial<T> extends MessageHandler {
        /**
         * Called when the next part of a message has been fully received.
         * 
         * @param partialMessage
         *            the partial message data.
         * @param last
         *            flag to indicate if this partialMessage is the last of the whole message being delivered.
         */
        void onMessage(T partialMessage, boolean last);
    }

    /**
     * This kind of handler is notified by the container on arrival of a complete message. If the message is received in
     * parts, the container buffers it until it is has been fully received before this method is called.
     * <p/>
     * For handling incoming text messages, the allowed types for T are
     * <ul>
     * <li>String</li>
     * <li>Reader</li>
     * <li>any developer object for which there is a corresponding Decoder.Text or Decoder.TextStream configured</li>
     * </ul>
     * <p/>
     * For handling incoming binary messages, the allowed types for T are
     * <ul>
     * <li>ByteBuffer</li>
     * <li>byte[]</li>
     * <li>InputStream</li>
     * <li>any developer object for which there is a corresponding Decoder.Binary or Decoder.BinaryStream configured</li>
     * </ul>
     * <p/>
     * For handling incoming pong messages, the type of T is PongMessage
     * <p/>
     * Developers should not continue to reference message objects of type Reader, ByteBuffer or InputStream after the
     * completion of the onMessage() call, since they may be recycled by the implementation.
     * 
     * @param <T>
     *            The type of the message object that this MessageHandler will consume.
     */
    public static interface Whole<T> extends MessageHandler {
        /**
         * Called when the message has been fully received.
         * 
         * @param message
         *            the message data.
         */
        void onMessage(T message);
    }
}
