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
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package javax.ws.rs;

import javax.ws.rs.core.Response;
import java.net.URI;


public class RedirectionException extends WebApplicationException {

    private static final long serialVersionUID = -2584325408291098012L;


    public RedirectionException(Response.Status status, URI location) {
        super((Throwable) null, validate(Response.status(status).location(location).build(), Response.Status.Family.REDIRECTION));
    }


    public RedirectionException(String message, Response.Status status, URI location) {
        super(message, null, validate(Response.status(status).location(location).build(), Response.Status.Family.REDIRECTION));
    }


    public RedirectionException(int status, URI location) {
        super((Throwable) null, validate(Response.status(status).location(location).build(), Response.Status.Family.REDIRECTION));
    }


    public RedirectionException(String message, int status, URI location) {
        super(message, null, validate(Response.status(status).location(location).build(), Response.Status.Family.REDIRECTION));
    }


    public RedirectionException(Response response) {
        super((Throwable) null, validate(response, Response.Status.Family.REDIRECTION));
    }


    public RedirectionException(String message, Response response) {
        super(message, null, validate(response, Response.Status.Family.REDIRECTION));
    }


    public URI getLocation() {
        return getResponse().getLocation();
    }
}
