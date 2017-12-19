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


public class ProcessingException extends RuntimeException {

    private static final long serialVersionUID = -4232431597816056514L;


    public ProcessingException(final Throwable cause) {
        super(cause);
    }


    public ProcessingException(final String message, final Throwable cause) {
        super(message, cause);
    }


    public ProcessingException(final String message) {
        super(message);
    }
}
