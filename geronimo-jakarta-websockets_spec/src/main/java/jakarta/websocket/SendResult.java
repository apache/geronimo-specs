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
 * The result of asynchronously sending a web socket message. A SendResult is either ok indicating there was no problem,
 * or is not OK in which case there was a problem and it carries an exception to indicate what the problem was.
 */
public class SendResult {

    private final Throwable exception;

    /**
     * Construct a SendResult signifying a successful send carrying no exception.
     */
    public SendResult() {
        exception = null;
    }

    /**
     * Construct a SendResult carrying an exception.
     * 
     * @param exception
     *            the exception causing a send failure.
     */
    public SendResult(Throwable exception) {
        this.exception = exception;
    }

    /**
     * The problem sending the message.
     * 
     * @return the problem or null if the send was successful.
     */
    public Throwable getException() {
        return this.exception;
    }

    /**
     * Determines if this result is ok or not.
     * 
     * @return whether the send was successful or not.
     */
    public boolean isOK() {
        return this.exception == null;
    }
}
