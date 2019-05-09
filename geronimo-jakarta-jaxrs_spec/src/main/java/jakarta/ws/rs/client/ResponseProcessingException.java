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

package jakarta.ws.rs.client;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;

public class ResponseProcessingException extends ProcessingException {

    private static final long serialVersionUID = -4923161617935731839L;

    private final Response response;

    public ResponseProcessingException(Response response, Throwable cause) {
        super(cause);
        this.response = response;
    }

    public ResponseProcessingException(Response response, String message, Throwable cause) {
        super(message, cause);
        this.response = response;
    }

    public ResponseProcessingException(Response response, String message) {
        super(message);
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
