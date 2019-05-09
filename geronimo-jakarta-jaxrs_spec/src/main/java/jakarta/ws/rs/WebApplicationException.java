/*
 * #%L
 * Apache Geronimo JAX-RS Spec 2.0
 * %%
 * Copyright (C) 2003 - 2014 The Apache Software Foundation
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package jakarta.ws.rs;

import jakarta.ws.rs.core.Response;

public class WebApplicationException extends RuntimeException {

    private static final long serialVersionUID = 8273970399584007146L;

    private final Response response;

    public WebApplicationException() {
        this((Throwable) null, Response.Status.INTERNAL_SERVER_ERROR);
    }

    public WebApplicationException(String message) {
        this(message, null, Response.Status.INTERNAL_SERVER_ERROR);
    }

    public WebApplicationException(final Response response) {
        this((Throwable) null, response);
    }

    public WebApplicationException(final String message, final Response response) {
        this(message, null, response);
    }

    public WebApplicationException(final int status) {
        this((Throwable) null, status);
    }

    public WebApplicationException(final String message, final int status) {
        this(message, null, status);
    }

    public WebApplicationException(final Response.Status status) {
        this((Throwable) null, status);
    }

    public WebApplicationException(final String message, final Response.Status status) {
        this(message, null, status);
    }

    public WebApplicationException(final Throwable cause) {
        this(cause, Response.Status.INTERNAL_SERVER_ERROR);
    }

    public WebApplicationException(final String message, final Throwable cause) {
        this(message, cause, Response.Status.INTERNAL_SERVER_ERROR);
    }

    public WebApplicationException(final Throwable cause, final Response response) {
        this(computeExceptionMessage(response), cause, response);
    }

    public WebApplicationException(final String message, final Throwable cause, final Response response) {
        super(message, cause);
        if (response == null) {
            this.response = Response.serverError().build();
        } else {
            this.response = response;
        }
    }

    private static String computeExceptionMessage(Response response) {
        final Response.StatusType statusInfo;
        if (response != null) {
            statusInfo = response.getStatusInfo();
        } else {
            statusInfo = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return "HTTP " + statusInfo.getStatusCode() + ' ' + statusInfo.getReasonPhrase();
    }

    public WebApplicationException(final Throwable cause, final int status) {
        this(cause, Response.status(status).build());
    }

    public WebApplicationException(final String message, final Throwable cause, final int status) {
        this(message, cause, Response.status(status).build());
    }

    public WebApplicationException(final Throwable cause, final Response.Status status) throws IllegalArgumentException {
        this(cause, Response.status(status).build());
    }

    public WebApplicationException(final String message, final Throwable cause, final Response.Status status)
            throws IllegalArgumentException {
        this(message, cause, Response.status(status).build());
    }

    public Response getResponse() {
        return response;
    }

    static Response validate(final Response response, Response.Status expectedStatus) {
        if (expectedStatus.getStatusCode() != response.getStatus()) {
            throw new IllegalArgumentException(String.format("Invalid response status code. Expected [%d], was [%d].",
                    expectedStatus.getStatusCode(), response.getStatus()));
        }
        return response;
    }

    static Response validate(final Response response, Response.Status.Family expectedStatusFamily) {
        if (response.getStatusInfo().getFamily() != expectedStatusFamily) {
            throw new IllegalArgumentException(
                    String.format("Status code of the supplied response [%d] is not from the required status code family \"%s\".",
                            response.getStatus(), expectedStatusFamily));
        }
        return response;
    }
}
