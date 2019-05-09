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

public class ClientErrorException extends WebApplicationException {

    private static final long serialVersionUID = -4101970664444907990L;

    public ClientErrorException(Response.Status status) {
        super((Throwable) null, validate(Response.status(status).build(), Response.Status.Family.CLIENT_ERROR));
    }

    public ClientErrorException(String message, Response.Status status) {
        super(message, null, validate(Response.status(status).build(), Response.Status.Family.CLIENT_ERROR));
    }

    public ClientErrorException(int status) {
        super((Throwable) null, validate(Response.status(status).build(), Response.Status.Family.CLIENT_ERROR));
    }

    public ClientErrorException(String message, int status) {
        super(message, null, validate(Response.status(status).build(), Response.Status.Family.CLIENT_ERROR));
    }

    public ClientErrorException(Response response) {
        super((Throwable) null, validate(response, Response.Status.Family.CLIENT_ERROR));
    }

    public ClientErrorException(String message, Response response) {
        super(message, null, validate(response, Response.Status.Family.CLIENT_ERROR));
    }

    public ClientErrorException(Response.Status status, Throwable cause) {
        super(cause, validate(Response.status(status).build(), Response.Status.Family.CLIENT_ERROR));
    }

    public ClientErrorException(String message, Response.Status status, Throwable cause) {
        super(message, cause, validate(Response.status(status).build(), Response.Status.Family.CLIENT_ERROR));
    }

    public ClientErrorException(int status, Throwable cause) {
        super(cause, validate(Response.status(status).build(), Response.Status.Family.CLIENT_ERROR));
    }

    public ClientErrorException(String message, int status, Throwable cause) {
        super(message, cause, validate(Response.status(status).build(), Response.Status.Family.CLIENT_ERROR));
    }

    public ClientErrorException(Response response, Throwable cause) {
        super(cause, validate(response, Response.Status.Family.CLIENT_ERROR));
    }

    public ClientErrorException(String message, Response response, Throwable cause) {
        super(message, cause, validate(response, Response.Status.Family.CLIENT_ERROR));
    }
}
