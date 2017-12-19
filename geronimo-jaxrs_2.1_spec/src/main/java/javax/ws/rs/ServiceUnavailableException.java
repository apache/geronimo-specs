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
import javax.ws.rs.ext.RuntimeDelegate;
import java.util.Date;

import static javax.ws.rs.core.HttpHeaders.RETRY_AFTER;
import static javax.ws.rs.core.Response.Status.SERVICE_UNAVAILABLE;


public class ServiceUnavailableException extends ServerErrorException {

    private static final long serialVersionUID = 3821068205617492633L;


    public ServiceUnavailableException() {
        super(Response.status(SERVICE_UNAVAILABLE).build());
    }


    public ServiceUnavailableException(String message) {
        super(message, Response.status(SERVICE_UNAVAILABLE).build());
    }


    public ServiceUnavailableException(Long retryAfter) {
        super(Response.status(SERVICE_UNAVAILABLE).header(RETRY_AFTER, retryAfter).build());
    }


    public ServiceUnavailableException(String message, Long retryAfter) {
        super(message, Response.status(SERVICE_UNAVAILABLE).header(RETRY_AFTER, retryAfter).build());
    }


    public ServiceUnavailableException(Date retryAfter) {
        super(Response.status(SERVICE_UNAVAILABLE).header(RETRY_AFTER, retryAfter).build());
    }


    public ServiceUnavailableException(String message, Date retryAfter) {
        super(message, Response.status(SERVICE_UNAVAILABLE).header(RETRY_AFTER, retryAfter).build());
    }


    public ServiceUnavailableException(Response response) {
        super(validate(response, SERVICE_UNAVAILABLE));
    }


    public ServiceUnavailableException(String message, Response response) {
        super(message, validate(response, SERVICE_UNAVAILABLE));
    }


    public ServiceUnavailableException(Date retryAfter, Throwable cause) {
        super(Response.status(SERVICE_UNAVAILABLE).header(RETRY_AFTER, retryAfter).build(), cause);
    }


    public ServiceUnavailableException(String message, Date retryAfter, Throwable cause) {
        super(message, Response.status(SERVICE_UNAVAILABLE).header(RETRY_AFTER, retryAfter).build(), cause);
    }


    public ServiceUnavailableException(Long retryAfter, Throwable cause) {
        super(Response.status(SERVICE_UNAVAILABLE).header(RETRY_AFTER, retryAfter).build(), cause);
    }


    public ServiceUnavailableException(String message, Long retryAfter, Throwable cause) {
        super(message, Response.status(SERVICE_UNAVAILABLE).header(RETRY_AFTER, retryAfter).build(), cause);
    }


    public ServiceUnavailableException(Response response, Throwable cause) {
        super(validate(response, SERVICE_UNAVAILABLE), cause);
    }


    public ServiceUnavailableException(String message, Response response, Throwable cause) {
        super(message, validate(response, SERVICE_UNAVAILABLE), cause);
    }


    public boolean hasRetryAfter() {
        return getResponse().getHeaders().containsKey(RETRY_AFTER);
    }


    public Date getRetryTime(final Date requestTime) {
        final String value = getResponse().getHeaderString(RETRY_AFTER);
        if (value == null) {
            return null;
        }

        try {
            Long interval = Long.parseLong(value);
            return new Date(requestTime.getTime() + interval * 1000);
        } catch (NumberFormatException ex) {

        }

        final RuntimeDelegate.HeaderDelegate<Date> dateDelegate = RuntimeDelegate.getInstance().createHeaderDelegate(Date.class);
        return dateDelegate.fromString(value);
    }
}
