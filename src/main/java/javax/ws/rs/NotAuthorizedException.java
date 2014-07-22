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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static javax.ws.rs.core.HttpHeaders.WWW_AUTHENTICATE;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;


public class NotAuthorizedException extends ClientErrorException {

    private static final long serialVersionUID = -3156040750581929702L;

    private transient List<Object> challenges;


    public NotAuthorizedException(Object challenge, Object... moreChallenges) {
        super(createUnauthorizedResponse(challenge, moreChallenges));
        this.challenges = cacheChallenges(challenge, moreChallenges);
    }


    public NotAuthorizedException(String message, Object challenge, Object... moreChallenges) {
        super(message, createUnauthorizedResponse(challenge, moreChallenges));
        this.challenges = cacheChallenges(challenge, moreChallenges);
    }


    public NotAuthorizedException(Response response) {
        super(validate(response, UNAUTHORIZED));
    }


    public NotAuthorizedException(String message, Response response) {
        super(message, validate(response, UNAUTHORIZED));
    }


    public NotAuthorizedException(Throwable cause, Object challenge, Object... moreChallenges) {
        super(createUnauthorizedResponse(challenge, moreChallenges), cause);
        this.challenges = cacheChallenges(challenge, moreChallenges);
    }


    public NotAuthorizedException(String message, Throwable cause, Object challenge, Object... moreChallenges) {
        super(message, createUnauthorizedResponse(challenge, moreChallenges), cause);
        this.challenges = cacheChallenges(challenge, moreChallenges);
    }


    public NotAuthorizedException(Response response, Throwable cause) {
        super(validate(response, UNAUTHORIZED), cause);
    }


    public NotAuthorizedException(String message, Response response, Throwable cause) {
        super(message, validate(response, UNAUTHORIZED), cause);
    }


    public List<Object> getChallenges() {
        if (challenges == null) {
            this.challenges = getResponse().getHeaders().get(WWW_AUTHENTICATE);
        }
        return challenges;
    }

    private static Response createUnauthorizedResponse(Object challenge, Object[] otherChallenges) {
        if (challenge == null) {
            throw new NullPointerException("Primary challenge parameter must not be null.");
        }

        Response.ResponseBuilder builder = Response.status(UNAUTHORIZED).header(WWW_AUTHENTICATE, challenge);

        if (otherChallenges != null) {
            for (Object oc : otherChallenges) {
                builder.header(WWW_AUTHENTICATE, oc);
            }
        }

        return builder.build();
    }

    private static List<Object> cacheChallenges(Object challenge, Object[] moreChallenges) {
        List<Object> temp = new ArrayList<Object>(1 + ((moreChallenges == null) ? 0 : moreChallenges.length));
        temp.add(challenge);
        if (moreChallenges != null) {
            temp.addAll(Arrays.asList(moreChallenges));
        }
        return Collections.unmodifiableList(temp);
    }
}
