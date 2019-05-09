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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

public class NotAllowedException extends ClientErrorException {

    private static final long serialVersionUID = -586776054369626119L;

    public NotAllowedException(String allowed, String... moreAllowed) {
        super(validateAllow(createNotAllowedResponse(allowed, moreAllowed)));
    }

    public NotAllowedException(String message, String allowed, String... moreAllowed) {
        super(message, validateAllow(createNotAllowedResponse(allowed, moreAllowed)));
    }

    private static Response createNotAllowedResponse(String allowed, String... moreAllowed) {
        if (allowed == null) {
            throw new NullPointerException("No allowed method specified.");
        }
        Set<String> methods;
        if (moreAllowed != null && moreAllowed.length > 0) {
            methods = new HashSet<String>(moreAllowed.length + 1);
            methods.add(allowed);
            Collections.addAll(methods, moreAllowed);
        } else {
            methods = Collections.singleton(allowed);
        }

        return Response.status(Response.Status.METHOD_NOT_ALLOWED).allow(methods).build();
    }

    public NotAllowedException(Response response) {
        super(validate(response, Response.Status.METHOD_NOT_ALLOWED));
    }

    public NotAllowedException(String message, Response response) {
        super(message, validate(response, Response.Status.METHOD_NOT_ALLOWED));
    }

    public NotAllowedException(Throwable cause, String... allowedMethods) {
        super(validateAllow(Response.status(Response.Status.METHOD_NOT_ALLOWED).allow(allowedMethods).build()), cause);
    }

    public NotAllowedException(String message, Throwable cause, String... allowedMethods) {
        super(message, validateAllow(Response.status(Response.Status.METHOD_NOT_ALLOWED).allow(allowedMethods).build()), cause);
    }

    public NotAllowedException(Response response, Throwable cause) {
        super(validateAllow(validate(response, Response.Status.METHOD_NOT_ALLOWED)), cause);
    }

    public NotAllowedException(String message, Response response, Throwable cause) {
        super(message, validateAllow(validate(response, Response.Status.METHOD_NOT_ALLOWED)), cause);
    }

    private static Response validateAllow(final Response response) {
        if (!response.getHeaders().containsKey(HttpHeaders.ALLOW)) {
            throw new IllegalArgumentException("Response does not contain required 'Allow' HTTP header.");
        }

        return response;
    }
}
