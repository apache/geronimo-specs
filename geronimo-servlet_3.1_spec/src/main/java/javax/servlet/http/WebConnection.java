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

package javax.servlet.http;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;

/**
 * 
 * This interface encapsulates the connection for an upgrade request. It allows the protocol handler to send service
 * requests and status queries to the container.
 * 
 * @since Servlet 3.1
 */
public interface WebConnection extends AutoCloseable {

    /**
     * Returns an input stream for this web connection.
     * 
     * @return a ServletInputStream for reading binary data
     * @throws IOException
     *             if an I/O error occurs
     */
    ServletInputStream getInputStream() throws IOException;

    /**
     * Returns an output stream for this web connection.
     * 
     * @return a ServletOutputStream for writing binary data
     * @throws IOException
     *             if an I/O error occurs
     */
    ServletOutputStream getOutputStream() throws IOException;
}
