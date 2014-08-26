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

package javax.mail;

/**
 * @version $Rev$ $Date$
 */
public class MessagingException extends Exception {

    private static final long serialVersionUID = -7569192289819959253L;

    // Required because serialization expects it to be here
    private Exception next;

    public MessagingException() {
        super();
    }

    public MessagingException(final String message) {
        super(message);
    }

    public MessagingException(final String message, final Exception cause) {
        super(message, cause);
        next = cause;
    }

    public synchronized Exception getNextException() {
        return next;
    }

    public synchronized boolean setNextException(final Exception cause) {
        if (next == null) {
            next = cause;
            return true;
        } else if (next instanceof MessagingException) {
            return ((MessagingException) next).setNextException(cause);
        } else {
            return false;
        }
    }

    @Override
    public String getMessage() {
        final Exception next = getNextException();
        if (next == null) {
            return super.getMessage();
        } else {
            return super.getMessage()
                    + " ("
                    + next.getClass().getName()
                    + ": "
                    + next.getMessage()
                    + ")";
        }
    }
    
    /**
     * MessagingException uses the nextException to provide a legacy chained throwable.
     * override the getCause method to return the nextException.
     */
    @Override
    public synchronized  Throwable getCause() {
        return next;
    }
}
