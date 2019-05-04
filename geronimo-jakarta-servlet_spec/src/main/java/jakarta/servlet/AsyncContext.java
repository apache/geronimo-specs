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

/**
 * @version $Rev$ $Date$
 * @since 3.0
 */
public interface AsyncContext {

    String ASYNC_CONTEXT_PATH = "javax.servlet.async.context_path";
    String ASYNC_PATH_INFO = "javax.servlet.async.path_info";
    String ASYNC_QUERY_STRING = "javax.servlet.async.query_string";
    String ASYNC_REQUEST_URI = "javax.servlet.async.request_uri";
    String ASYNC_SERVLET_PATH = "javax.servlet.async.servlet_path";

    void addListener(AsyncListener listener) throws IllegalStateException;

    void addListener(AsyncListener listener, ServletRequest request, ServletResponse response) throws IllegalStateException;

    void complete();

    <T extends AsyncListener> T createListener(Class<T> clazz) throws ServletException;

    void dispatch() throws IllegalStateException;

    void dispatch(ServletContext servletContext, String path) throws IllegalStateException;

    void dispatch(String path) throws IllegalStateException;

    ServletRequest getRequest();

    ServletResponse getResponse();

    /**
     * Returns current timeout value in milliseconds. Zero or less means no timeout.
     * If setTimeout has not been called, the container-specific default timeout is returned.
     *
     * @return timeout in milliseconds
     */
    long getTimeout();

    boolean hasOriginalRequestAndResponse();

    void setTimeout(long timeoutMilliseconds) throws IllegalStateException;

    void start(Runnable run);
}
