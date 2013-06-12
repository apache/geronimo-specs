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

/**
 * This annotation may be used to annotate method parameters on server endpoints where a URI-template has been used in
 * the path-mapping of the ServerEndpoint annotation. The method parameter may be of type String, any Java primitive
 * type or any boxed version thereof. If a client URI matches the URI-template, but the requested path parameter cannot
 * be decoded, then the websocket's error handler will be called.
 * <p/>
 * For example:-
 * <p/>
 * <code>
 * 
 * @ServerEndpoint("/bookings/{guest-id ") public class BookingServer {
 * 
 * @OnMessage public void processBookingRequest(@PathParam("guest-id") String guestID, String message, Session session)
 *            { // process booking from the given guest here } } </code>
 *            <p/>
 *            For example:-
 *            <p/>
 *            <code>
 * @ServerEndpoint("/rewards/{vip-level ") public class RewardServer {
 * 
 * @OnMessage public void processReward(@PathParam("vip-level") Integer vipLevel, String message, Session session) {
 *            // process reward here } } </code>
 * 
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.PARAMETER)
public @interface PathParam {

    /**
     * The name of the variable used in the URI-template. If the name does not match a path variable in the
     * URI-template, the value of the method parameter this annotation annotates is null.
     * 
     * @return the name of the variable used in the URI-template.
     */
    public String value();
}
