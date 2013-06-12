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
 * The ClientEndpoint annotation a class level annotation is used to denote that a POJO is a web socket client and can
 * be deployed as such. Similar to ServerEndpoint, POJOs that are annotated with this annotation can have methods that,
 * using the web socket method level annotations, are web socket lifecycle methods.
 * <p/>
 * For example:
 * <p/>
 * <code>
 * 
 * @ClientEndpoint(subprotocols="chat") public class HelloServer {
 * 
 * @OnMessage public void processMessageFromServer(String message, Session session) {
 *            System.out.println("Message came from the server ! " + message); }
 * 
 *            } </code>
 * 
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface ClientEndpoint {

    /**
     * An optional custom configurator class that the developer would like to use to provide custom configuration of new
     * instances of this endpoint. The implementation creates a new instance of the configurator per logical endpoint.
     * 
     * @return the custom configurator class, or ClientEndpointConfigurator.class if none was provided in the
     *         annotation.
     */
    Class<? extends ClientEndpointConfig.Configurator> configurator() default ClientEndpointConfig.Configurator.class;

    /**
     * The array of Java classes that are to act as Decoders for messages coming into the client.
     * 
     * @return the array of decoders.
     */
    Class<? extends Decoder>[] decoders() default {};

    /**
     * The array of Java classes that are to act as Encoders for messages sent by the client.
     * 
     * @return the array of decoders.
     */
    Class<? extends Encoder>[] encoders() default {};

    /**
     * The names of the subprotocols this client supports.
     * 
     * @return the array of names of the subprotocols.
     */
    String[] subprotocols() default {};
}
