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

package jakarta.websocket;

/**
 * A SessionException represents a general exception type reporting problems occurring on a websocket session.
 */
public class SessionException extends Exception {
    private static final long serialVersionUID = -5781258978872105957L;

    private final Session session;

    /**
     * Creates a new instance of this exception with the given message, the wrapped cause of the exception and the
     * session with which the problem is associated.
     * 
     * @param message
     *            a description of the problem
     * @param cause
     *            the error that caused the problem
     * @param session
     *            the session on which the problem occurred.
     */
    public SessionException(String message, Throwable cause, Session session) {
        super(message, cause);
        this.session = session;
    }

    /**
     * Return the Session on which the problem occurred.
     * 
     * @return the session
     */
    public Session getSession() {
        return this.session;
    }
}
