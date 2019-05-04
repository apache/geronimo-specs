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

package javax.websocket.server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.websocket.Decoder;
import javax.websocket.Encoder;

/**
 * This class level annotation declares that the class it decorates is a web socket endpoint that will be deployed and
 * made available in the URI-space of a web socket server. The annotation allows the developer to define the URL (or URI
 * template) which this endpoint will be published, and other important properties of the endpoint to the websocket
 * runtime, such as the encoders it uses to send messages.
 * <p/>
 * The annotated class must have a public no-arg constructor.
 * <p/>
 * For example:
 * </p>
 * <code>
 * 
 * @ServerEndpoint("/hello"); public class HelloServer {
 * 
 * @OnMessage public void processGreeting(String message, Session session) { System.out.println("Greeting received:" +
 *            message); }
 * 
 *            } </code>
 * 
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface ServerEndpoint {

    /**
     * The URI or URI-template, level-1 (See RFC 6570) where the endpoint will be deployed. The URI us relative to the
     * root of the web socket container and must begin with a leading "/". Trailing "/"'s are ignored. Examples:
     * <p/>
     * <code>
     * 
     * @ServerEndpoint("/chat")
     * @ServerEndpoint("/chat/{user ")
     * @ServerEndpoint("/booking/{privilege-level ") </code>
     * 
     * @return the URI or URI-template
     */
    public String value();

    /**
     * The ordered array of web socket protocols this endpoint supports. For example, {"superchat", "chat"}.
     * 
     * @return the subprotocols.
     */
    public String[] subprotocols() default {};

    /**
     * The ordered array of decoder classes this endpoint will use. For example, if the developer has provided a
     * MysteryObject decoder, this endpoint will be able to receive MysteryObjects as web socket messages. The websocket
     * runtime will use the first decoder in the list able to decode a message, ignoring the remaining decoders.
     * 
     * @return the decoders.
     */
    public Class<? extends Decoder>[] decoders() default {};

    /**
     * The ordered array of encoder classes this endpoint will use. For example, if the developer has provided a
     * MysteryObject encoder, this class will be able to send web socket messages in the form of MysteryObjects. The
     * websocket runtime will use the first encoder in the list able to encode a message, ignoring the remaining
     * encoders.
     * 
     * @return the encoders.
     */
    public Class<? extends Encoder>[] encoders() default {};

    /**
     * The optional custom configurator class that the developer would like to use to further configure new instances of
     * this endpoint. If no configurator class is provided, the implementation uses its own. The implementation creates
     * a new instance of the configurator per logical endpoint.
     * 
     * @return the custom configuration class, or ServerEndpointConfig.Configurator.class if none was set in the
     *         annotation.
     */
    public Class<? extends ServerEndpointConfig.Configurator> configurator() default ServerEndpointConfig.Configurator.class;
}
