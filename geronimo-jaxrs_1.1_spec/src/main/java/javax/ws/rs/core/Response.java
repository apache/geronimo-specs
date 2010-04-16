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

package javax.ws.rs.core;

import java.net.URI;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.ext.RuntimeDelegate;

public abstract class Response {

    private final static RuntimeDelegate delegate = RuntimeDelegate.getInstance();

    public abstract static class ResponseBuilder {
        protected ResponseBuilder() {
            // do nothing
        }

        public abstract Response build();

        public abstract Response.ResponseBuilder cacheControl(CacheControl value);

        @Override
        public abstract Response.ResponseBuilder clone();

        public abstract Response.ResponseBuilder contentLocation(URI value);

        public abstract Response.ResponseBuilder cookie(NewCookie... values);

        public abstract Response.ResponseBuilder entity(Object value);

        public abstract Response.ResponseBuilder expires(Date value);

        public abstract Response.ResponseBuilder header(String name, Object value);

        public abstract Response.ResponseBuilder language(Locale value);

        public abstract Response.ResponseBuilder language(String value);

        public abstract Response.ResponseBuilder lastModified(Date value);

        public abstract Response.ResponseBuilder location(URI value);

        protected static Response.ResponseBuilder newInstance() {
            return delegate.createResponseBuilder();
        }

        public abstract Response.ResponseBuilder status(int value);

        public Response.ResponseBuilder status(Response.Status value) {
            return delegate.createResponseBuilder().status(value.getStatusCode());
        }

        public Response.ResponseBuilder status(Response.StatusType status) {
            return delegate.createResponseBuilder().status(status.getStatusCode());
        }

        public abstract Response.ResponseBuilder tag(EntityTag value);

        public abstract Response.ResponseBuilder tag(String value);

        public abstract Response.ResponseBuilder type(MediaType value);

        public abstract Response.ResponseBuilder type(String type);

        public abstract Response.ResponseBuilder variant(Variant value);

        public abstract Response.ResponseBuilder variants(List<Variant> values);

    }

    public static enum Status implements StatusType {
        OK(Family.SUCCESSFUL, 200, "OK"),
        CREATED(Family.SUCCESSFUL, 201, "Created"),
        ACCEPTED(Family.SUCCESSFUL, 202, "Accepted"),
        NO_CONTENT(Family.SUCCESSFUL, 204, "No Content"),
        MOVED_PERMANENTLY(Family.REDIRECTION, 301, "Moved Permanently"),
        SEE_OTHER(Family.REDIRECTION, 303, "See Other"),
        NOT_MODIFIED(Family.REDIRECTION, 304, "Not Modified"),
        TEMPORARY_REDIRECT(Family.REDIRECTION, 307, "Temporary Redirect"),
        BAD_REQUEST(Family.CLIENT_ERROR, 400,"Bad Request"),
        UNAUTHORIZED(Family.CLIENT_ERROR, 401, "Unauthorized"),
        FORBIDDEN(Family.CLIENT_ERROR, 403, "Forbidden"),
        NOT_FOUND(Family.CLIENT_ERROR, 404, "Not Found"),
        NOT_ACCEPTABLE(Family.CLIENT_ERROR, 406, "Not Acceptable"),
        CONFLICT(Family.CLIENT_ERROR, 409, "Conflict"),
        GONE(Family.CLIENT_ERROR, 410, "Gone"),
        PRECONDITION_FAILED(Family.CLIENT_ERROR, 412, "Precondition Failed"),
        UNSUPPORTED_MEDIA_TYPE(Family.CLIENT_ERROR, 415, "Unsupported Media Type"),
        INTERNAL_SERVER_ERROR(Family.SERVER_ERROR, 500, "Internal Server Error"),
        SERVICE_UNAVAILABLE(Family.SERVER_ERROR, 503, "Service Unavailable"), ;

        private final Family family;
        private final int    statusCode;
        private final String reasonPhrase;

        private Status(Family family, int statusCode, String reasonPhrase) {
            this.family = family;
            this.statusCode = statusCode;
            this.reasonPhrase = reasonPhrase;
        }

        public static enum Family {
            INFORMATIONAL,
            SUCCESSFUL,
            REDIRECTION,
            CLIENT_ERROR,
            SERVER_ERROR,
            OTHER
        }

        public static Status fromStatusCode(int statusCode) {
            for (Status s : values()) {
                if (s.getStatusCode() == statusCode) {
                    return s;
                }
            }
            return null;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public Response.Status.Family getFamily() {
            return family;
        }

        @Override
        public String toString() {
            return reasonPhrase;
        }

        public String getReasonPhrase() {
            return reasonPhrase;
        }
    }

    public interface StatusType {
        public Response.Status.Family getFamily();

        public String getReasonPhrase();

        public int getStatusCode();
    }

    protected Response() {
        // do nothing
    }

    public static Response.ResponseBuilder created(java.net.URI location) {
        if (location == null) {
            throw new IllegalArgumentException();
        }
        return status(Status.CREATED).location(location);
    }

    public static Response.ResponseBuilder fromResponse(Response response) {
        ResponseBuilder builder = delegate.createResponseBuilder();
        builder.status(response.getStatus());
        builder.entity(response.getEntity());
        MultivaluedMap<String, Object> metadata = response.getMetadata();
        for (String key : metadata.keySet()) {
            List<Object> values = metadata.get(key);
            for (Object value : values) {
                builder.header(key, value);
            }
        }
        return builder;
    }

    public abstract Object getEntity();

    public abstract MultivaluedMap<String, Object> getMetadata();

    public abstract int getStatus();

    public static Response.ResponseBuilder noContent() {
        return status(Status.NO_CONTENT);
    }

    public static Response.ResponseBuilder notAcceptable(List<Variant> values) {
        ResponseBuilder builder = status(Status.NOT_ACCEPTABLE);
        if (values == null) {
            return builder.variants(Collections.EMPTY_LIST);
        }
        return builder.variants(values);
    }

    public static Response.ResponseBuilder notModified() {
        return status(Status.NOT_MODIFIED);
    }

    public static Response.ResponseBuilder notModified(EntityTag value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        return status(Status.NOT_MODIFIED).tag(value);
    }

    public static Response.ResponseBuilder notModified(String value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        return status(Status.NOT_MODIFIED).tag(value);
    }

    public static Response.ResponseBuilder ok() {
        return status(Status.OK);
    }

    public static Response.ResponseBuilder ok(Object entity) {
        return status(Status.OK).entity(entity);
    }

    public static Response.ResponseBuilder ok(Object entity, MediaType mediaType) {
        return status(Status.OK).entity(entity).type(mediaType);
    }

    public static Response.ResponseBuilder ok(Object entity, String mediaType) {
        return status(Status.OK).entity(entity).type(mediaType);
    }

    public static Response.ResponseBuilder ok(Object entity, Variant variant) {
        return status(Status.OK).entity(entity).variant(variant);
    }

    public static Response.ResponseBuilder seeOther(URI location) {
        if (location == null) {
            throw new IllegalArgumentException();
        }
        return status(Status.SEE_OTHER).location(location);
    }

    public static Response.ResponseBuilder serverError() {
        return status(Status.INTERNAL_SERVER_ERROR);
    }

    public static Response.ResponseBuilder status(int status) {
        if (status < 100 || status > 599) {
            throw new IllegalArgumentException();
        }
        return ResponseBuilder.newInstance().status(status);
    }

    public static Response.ResponseBuilder status(Response.Status status) {
        if (status == null) {
            throw new IllegalArgumentException();
        }
        return ResponseBuilder.newInstance().status(status);
    }

    public static Response.ResponseBuilder status(Response.StatusType status) {
        if (status == null) {
            throw new IllegalArgumentException();
        }
        return ResponseBuilder.newInstance().status(status);
    }

    public static Response.ResponseBuilder temporaryRedirect(URI location) {
        return status(Status.TEMPORARY_REDIRECT).location(location);
    }
}
