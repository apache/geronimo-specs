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

package javax.servlet;

import java.io.IOException;
import java.io.InputStream;

/**
 * Provides an input stream for reading binary data from a client
 * request, including an efficient <code>readLine</code> method
 * for reading data one line at a time. With some protocols, such
 * as HTTP POST and PUT, a <code>ServletInputStream</code>
 * object can be used to read data sent from the client.
 * <p/>
 * <p>A <code>ServletInputStream</code> object is normally retrieved via
 * the {@link ServletRequest#getInputStream} method.
 * <p/>
 * <p/>
 * <p>This is an abstract class that a servlet container implements.
 * Subclasses of this class
 * must implement the <code>java.io.InputStream.read()</code> method.
 *
 * @version $Rev$ $Date$
 * @see                ServletRequest
 */

public abstract class ServletInputStream extends InputStream {

    /**
     * Does nothing, because this is an abstract class.
     */
    protected ServletInputStream() {
    }

    /**
     * Returns true when all the data from the stream has been read else it returns false.
     * 
     * @return true when all data for this particular request has been read, otherwise returns false.
     * @since Servlet 3.1
     */
    public abstract boolean isFinished();

    /**
     * Returns true if data can be read without blocking else returns false.
     * 
     * @return true if data can be obtained without blocking, otherwise returns false.
     * @since Servlet 3.1
     */
    public abstract boolean isReady();

    /**
     * Reads the input stream, one line at a time. Starting at an offset, reads bytes into an array, until it reads a
     * certain number of bytes or reaches a newline character, which it reads into the array as well.
     * <p/>
     * <p>
     * This method returns -1 if it reaches the end of the input stream before reading the maximum number of bytes.
     * 
     * @param b
     *            an array of bytes into which data is read
     * @param off
     *            an integer specifying the character at which this method begins reading
     * @param len
     *            an integer specifying the maximum number of bytes to read
     * @throws IOException
     *             if an input or output exception has occurred
     * @return an integer specifying the actual number of bytes read, or -1 if the end of the stream is reached
     */
    public int readLine(byte[] b, int off, int len) throws IOException {

        if (len <= 0) {
            return 0;
        }
        int count = 0, c;

        while ((c = read()) != -1) {
            b[off++] = (byte) c;
            count++;
            if (c == '\n' || count == len) {
                break;
            }
        }
        return count > 0 ? count : -1;
    }

    /**
     * Instructs the ServletInputStream to invoke the provided ReadListener when it is possible to read.
     * 
     * @param readListener
     *            - the ReadListener that should be notified when it's possible to read.
     * @throws IllegalStateException
     *             - if one of the following conditions is true
     *             <ul>
     *             <li>the associated request is neither upgraded nor the async started</li>
     *             <li>setReadListener is called more than once within the scope of the same request.</li>
     *             </ul>
     * @throws NullPointerException
     *             - if readListener is null
     * @since Servlet 3.1
     */
    public abstract void setReadListener(ReadListener readListener);
}
