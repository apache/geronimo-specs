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
 * Callback notification mechanism that signals to the developer it's possible to write content without blocking.
 * 
 * 
 * @since Servlet 3.1
 */
public interface WriteListener extends EventListener {

    /**
     * Invoked when an error occurs writing data using the non-blocking APIs.
     */
    void onError(Throwable t);

    /**
     * When an instance of the WriteListener is registered with a ServletOutputStream, this method will be invoked by
     * the container the first time when it is possible to write data. Subsequently the container will invoke this
     * method if and only if ServletOutputStream.isReady() method has been called and has returned false.
     * 
     * @throws IOException
     *             - if an I/O related error has occurred during processing
     */
    void onWritePossible() throws IOException;
}
