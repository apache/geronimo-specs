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
import java.io.InputStream;
import java.util.Collection;

/**
 * multipart/form-data part or form item
 *
 * @version $Rev$ $Date$
 * @since 3.0
 */
public interface Part {

    /**
     * Deletes the underlying storage for a file item, including deleting any associated temporary disk file.
     * 
     * @throws IOException
     *             if an error occurs.
     */
    void delete() throws IOException;

    /**
     * Gets the content type of this part.
     * 
     * @return The content type of this part.
     */
    String getContentType();

    /**
     * Returns the value of the specified mime header as a String. If the Part did not include a header of the specified
     * name, this method returns null. If there are multiple headers with the same name, this method returns the first
     * header in the part. The header name is case insensitive. You can use this method with any request header.
     * 
     * @param headerName
     *            a String specifying the header name
     * @return a String containing the value of the requested header, or null if the part does not have a header of that
     *         name
     */
    String getHeader(String headerName);

    /**
     * Gets the header names of this Part.
     * <p>
     * Some servlet containers do not allow servlets to access headers using this method, in which case this method
     * returns null
     * <p/>
     * Any changes to the returned Collection must not affect this Part.
     * 
     * @return a (possibly empty) Collection of the header names of this Part
     */
    Collection<String> getHeaderNames();

    /**
     * Gets the values of the Part header with the given name.
     * <p/>
     * Any changes to the returned Collection must not affect this Part.
     * <p/>
     * Part header names are case insensitive.
     * 
     * @param headerNamethe
     *            header name whose values to return
     * @return a (possibly empty) Collection of the values of the header with the given name
     */
    Collection<String> getHeaders(String headerName);

    /**
     * Gets the content of this part as an InputStream
     * 
     * @return The content of this part as an InputStream
     * @throws IOException
     *             If an error occurs in retrieving the contet as an InputStream
     */
    InputStream getInputStream() throws IOException;

    /**
     * Gets the name of this part
     * 
     * @return The name of this part as a String
     */
    String getName();

    /**
     * Returns the size of this fille.
     * 
     * @return a long specifying the size of this part, in bytes.
     */
    long getSize();

    /**
     * Gets the file name specified by the client.
     * 
     * @return the submitted file name
     * @since Servlet 3.1
     */
    String getSubmittedFileName();

    /**
     * A convenience method to write this uploaded item to disk.
     * <p/>
     * This method is not guaranteed to succeed if called more than once for the same part. This allows a particular
     * implementation to use, for example, file renaming, where possible, rather than copying all of the underlying
     * data, thus gaining a significant performance benefit.
     * 
     * @param fileName
     *            the name of the file to which the stream will be written. The file is created relative to the location
     *            as specified in the MultipartConfig
     * @throws IOException
     *             if an error occurs.
     */
    void write(String fileName) throws IOException;
    
}
