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

//
// This source code implements specifications defined by the Java
// Community Process. In order to remain compliant with the specification
// DO NOT add / change / or delete method signatures!
//

package jakarta.ejb;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * @version $Rev$ $Date$
 */
public class EJBException extends RuntimeException {

    private static final long serialVersionUID = 796770993296843510L;
    private Exception causeException;

    public EJBException() {
        super();
    }

    public EJBException(Exception causeException) {
        super(causeException);
    }

    public EJBException(String message) {
        super(message);
    }

    public EJBException(String message, Exception causeException) {
        super(message, causeException);
    }

    public Exception getCausedByException() {
        Throwable cause = getCause();
        if (cause instanceof Exception) {
            return (Exception) cause;
        }
        return null;
    }

    public String getMessage() {
        return super.getMessage();
    }


    public void printStackTrace(PrintStream ps) {
        super.printStackTrace(ps);
    }


    public void printStackTrace() {
        super.printStackTrace();
    }


    public void printStackTrace(PrintWriter pw) {
        super.printStackTrace(pw);
    }
}
