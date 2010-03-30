/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package javax.ws.rs;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class WebApplicationException extends RuntimeException {

    private static final long serialVersionUID = 11660101L;

    private final Response    response;

    public WebApplicationException() {
        this(Status.INTERNAL_SERVER_ERROR);
    }

    public WebApplicationException(int status) {
        response = Response.status(status).build();
    }

    public WebApplicationException(Response.Status status) {
        response = Response.status(status).build();
    }

    public WebApplicationException(Response response) {
        this.response = response;
    }

    public WebApplicationException(Throwable cause) {
        this(cause, Status.INTERNAL_SERVER_ERROR);
    }

    public WebApplicationException(Throwable cause, int status) {
        super(cause);
        response = Response.status(status).build();
    }

    public WebApplicationException(Throwable cause, Response.Status status) {
        super(cause);
        response = Response.status(status).build();
    }

    public WebApplicationException(Throwable cause, Response response) {
        super(cause);
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
