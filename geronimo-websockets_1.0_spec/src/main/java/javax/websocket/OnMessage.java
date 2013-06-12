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

package javax.websocket;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This method level annotation can be used to make a Java method receive incoming web socket messages. Each websocket
 * endpoint may only have one message handling method for each of the native websocket message formats: text, binary and
 * pong. Methods using this annotation are allowed to have parameters of types described below, otherwise the container
 * will generate an error at deployment time.
 * <p/>
 * The allowed parameters are:
 * <ol>
 * <li>Exactly one of any of the following choices
 * <ul>
 * <li>if the method is handling text messages:
 * <ul>
 * <li>String to receive the whole message</li>
 * <li>Java primitive or class equivalent to receive the whole message converted to that type</li>
 * <li>String and boolean pair to receive the message in parts</li>
 * <li>Reader to receive the whole message as a blocking stream</li>
 * <li>any object parameter for which the endpoint has a text decoder (Decoder.Text or Decoder.TextStream).</li>
 * </ul>
 * </li>
 * <li>if the method is handling binary messages:
 * <ul>
 * <li>byte[] or ByteBuffer to receive the whole message</li>
 * <li>byte[] and boolean pair, or ByteBuffer and boolean pair to receive the message in parts</li>
 * <li>InputStream to receive the whole message as a blocking stream</li>
 * <li>any object parameter for which the endpoint has a binary decoder (Decoder.Binary or Decoder.BinaryStream).</li>
 * </ul>
 * </li>
 * <li>if the method is handling pong messages:
 * <ul>
 * <li>PongMessage for handling pong messages</li>
 * </ul>
 * </li>
 * </ul>
 * </li>
 * <li>and Zero to n String or Java primitive parameters annotated with the PathParam annotation for server endpoints.</li>
 * <li>and an optional Session parameter</li>
 * </ol>
 * <p/>
 * The parameters may be listed in any order.
 * <p/>
 * The method may have a non-void return type, in which case the web socket runtime must interpret this as a web socket
 * message to return to the peer. The allowed data types for this return type, other than void, are String, ByteBuffer,
 * byte[], any Java primitive or class equivalent, and anything for which there is an encoder. If the method uses a Java
 * primitive as a return value, the implementation must construct the text message to send using the standard Java
 * string representation of the Java primitive unless there developer provided encoder for the type configured for this
 * endpoint, in which case that encoder must be used. If the method uses a class equivalent of a Java primitive as a
 * return value, the implementation must construct the text message from the Java primitive equivalent as described
 * above.
 * <p/>
 * Developers should note that if developer closes the session during the invocation of a method with a return type, the
 * method will complete but the return value will not be delivered to the remote endpoint. The send failure will be
 * passed back into the endpoint's error handling method.
 * <p/>
 * For example:
 * <p/>
 * <code>
 * 
 * @OnMessage public void processGreeting(String message, Session session) { System.out.println("Greeting received:" +
 *            message); } </code>
 *            <p/>
 *            For example:
 *            <p/>
 *            <code>
 * @OnMessage public void processUpload(byte[] b, boolean last, Session session) {
 *            // process partial data here, which check on last to see if these is more on the way } </code>
 *            <p/>
 *            Developers should not continue to reference message objects of type Reader, ByteBuffer or InputStream
 *            after the annotated method has completed, since they may be recycled by the implementation.
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface OnMessage {
    long maxMessageSize() default -1L;
}
