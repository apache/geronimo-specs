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
import java.util.EventListener;

/**
 * This class represents a call-back mechanism that will notify implementations as HTTP request data becomes available
 * to be read without blocking.
 * 
 * @since Servlet 3.1
 */
public interface ReadListener extends EventListener {

    /**
     * When an instance of the ReadListener is registered with a ServletInputStream, this method will be invoked by the
     * container the first time when it is possible to read data. Subsequently the container will invoke this method if
     * and only if ServletInputStream.isReady() method has been called and has returned false.
     * 
     * @throws IOException
     *             - if an I/O related error has occurred during processing
     */
    void onAllDataRead() throws IOException;

    /**
     * When an instance of the ReadListener is registered with a ServletInputStream, this method will be invoked by the
     * container the first time when it is possible to read data. Subsequently the container will invoke this method if
     * and only if ServletInputStream.isReady() method has been called and has returned false.
     * 
     * @throws IOException
     *             - if an I/O related error has occurred during processing
     */
    void onDataAvailable() throws IOException;

    /**
     * Invoked when an error occurs processing the request.
     */
    void onError(Throwable t);
}
