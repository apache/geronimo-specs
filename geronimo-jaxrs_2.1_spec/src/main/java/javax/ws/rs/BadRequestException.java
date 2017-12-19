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


public class BadRequestException extends ClientErrorException {

    private static final long serialVersionUID = 7264647684649480265L;


    public BadRequestException() {
        super(Response.Status.BAD_REQUEST);
    }


    public BadRequestException(String message) {
        super(message, Response.Status.BAD_REQUEST);
    }


    public BadRequestException(Response response) {
        super(validate(response, Response.Status.BAD_REQUEST));
    }


    public BadRequestException(String message, Response response) {
        super(message, validate(response, Response.Status.BAD_REQUEST));
    }


    public BadRequestException(Throwable cause) {
        super(Response.Status.BAD_REQUEST, cause);
    }


    public BadRequestException(String message, Throwable cause) {
        super(message, Response.Status.BAD_REQUEST, cause);
    }


    public BadRequestException(Response response, Throwable cause) {
        super(validate(response, Response.Status.BAD_REQUEST), cause);
    }


    public BadRequestException(String message, Response response, Throwable cause) {
        super(message, validate(response, Response.Status.BAD_REQUEST), cause);
    }
}
