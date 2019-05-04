/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package javax.servlet.jsp;

/**
 * A generic exception known to the JSP engine; uncaught
 * JspExceptions will result in an invocation of the errorpage
 * machinery.
 */

public class JspException extends Exception {

    /**
     * Construct a JspException.
     */
    public JspException() {
    }


    /**
     * Constructs a new JSP exception with the
     * specified message. The message can be written 
     * to the server log and/or displayed for the user. 
     *
     * @param msg 		a <code>String</code> 
     *				specifying the text of 
     *				the exception message
     *
     */
    public JspException(String msg) {
	super(msg);
    }


    /**
     * Constructs a new JSP exception when the JSP 
     * needs to throw an exception and include a message 
     * about the "root cause" exception that interfered with its 
     * normal operation, including a description message.
     *
     *
     * @param message 		a <code>String</code> containing 
     *				the text of the exception message
     *
     * @param rootCause		the <code>Throwable</code> exception 
     *				that interfered with the servlet's
     *				normal operation, making this servlet
     *				exception necessary
     *
     */
    
    public JspException(String message, Throwable rootCause) {
	super(message, rootCause);
    }


    /**
     * Constructs a new JSP exception when the JSP 
     * needs to throw an exception and include a message
     * about the "root cause" exception that interfered with its
     * normal operation. 
     *
     *
     * @param rootCause 	the <code>Throwable</code> exception
     * 				that interfered with the JSP's
     *				normal operation, making the JSP exception
     *				necessary
     *
     */

    public JspException(Throwable rootCause) {
	super(rootCause);
    }

    
    /**
     * Returns the exception that caused this JSP exception.
     *
     *
     * @return			the <code>Throwable</code> 
     *				that caused this JSP exception
     *
     */
    public Throwable getRootCause() {
	return getCause();
    }
}
