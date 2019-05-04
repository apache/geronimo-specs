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

/**
 * A general exception that occurs when trying to encode a custom object to a string or binary message.
 */
public class EncodeException extends Exception {
    private static final long serialVersionUID = -446026355870506692L;

    private final Object object;

    /**
     * Constructor with the object being encoded, and the reason why it failed to be.
     * 
     * @param object
     *            the object that could not be encoded.
     * @param message
     *            the reason for the failure.
     */
    public EncodeException(Object object, String message) {
        super(message);
        this.object = object;
    }

    /**
     * Constructor with the object being encoded, and the reason why it failed to be, and the cause.
     * 
     * @param object
     *            the object that could not be encoded.
     * @param message
     *            the reason for the failure.
     * @param cause
     *            the cause of the problem.
     */
    public EncodeException(Object object, String message, Throwable cause) {
        super(message, cause);
        this.object = object;
    }

    /**
     * Return the Object that could not be encoded.
     * 
     * @return the object.
     */
    public Object getObject() {
        return this.object;
    }
}
