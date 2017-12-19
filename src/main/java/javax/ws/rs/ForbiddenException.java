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

package javax.ws.rs;

import javax.ws.rs.core.Response;

public class ForbiddenException extends ClientErrorException {

    private static final long serialVersionUID = -2740045367479165061L;

    public ForbiddenException() {
        super(Response.Status.FORBIDDEN);
    }

    public ForbiddenException(String message) {
        super(message, Response.Status.FORBIDDEN);
    }

    public ForbiddenException(Response response) {
        super(validate(response, Response.Status.FORBIDDEN));
    }

    public ForbiddenException(String message, Response response) {
        super(message, validate(response, Response.Status.FORBIDDEN));
    }

    public ForbiddenException(Throwable cause) {
        super(Response.Status.FORBIDDEN, cause);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, Response.Status.FORBIDDEN, cause);
    }

    public ForbiddenException(Response response, Throwable cause) {
        super(validate(response, Response.Status.FORBIDDEN), cause);
    }

    public ForbiddenException(String message, Response response, Throwable cause) {
        super(message, validate(response, Response.Status.FORBIDDEN), cause);
    }
}
