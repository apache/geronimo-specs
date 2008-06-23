/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package javax.util.concurrent;

import java.util.concurrent.ExecutionException;

/**
 * Exception indicating that the result of a value-producing task, cannot be retrieved because the
 * task run was aborted.<p>
 *
 * Use the {@link java.lang.Throwable#getCause()} method to determine why the task aborted.
 */
public class AbortedException extends ExecutionException {

    private static final long serialVersionUID = 8313294757663362709L;

    /**
     * Constructs an AbortedException with <code>null</code> as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a call to
     * {@link java.lang.Throwable#initCause(java.lang.Throwable)}.
     */
    public AbortedException() {
        super();
    }

    /**
     * Constructs an AbortedException exception with the specified detail message and cause.<p>
     *
     * Note that the detail message associated with cause is not automatically incorporated in
     * this exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link java.lang.Throwable#getMessage()} method).
     * @param cause the cause (which is saved for later retrieval by the {@link java.lang.Throwable#getCause()} method).
     * (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public AbortedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs an AbortedException exception with the specified detail message.<p>
     *
     * The cause is not initialized, and may subsequently be initialized by a call to
     * {@link java.lang.Throwable#initCause(java.lang.Throwable)}.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link java.lang.Throwable#getMessage()} method).
     */
    public AbortedException(String message) {
        super(message);
    }

    /**
     * Constructs an AbortedException exception with the specified cause and a
     * detail message of (cause==null ? null : cause.toString())
     * (which typically contains the class and detail message of cause).
     *
     * @param cause the cause (which is saved for later retrieval by the {@link java.lang.Throwable#getCause()} method).
     * (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public AbortedException(Throwable cause) {
        super(cause);
    }

}
