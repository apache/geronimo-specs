/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package jakarta.enterprise.context;

import jakarta.enterprise.context.spi.Context;
import jakarta.enterprise.context.spi.Contextual;
import jakarta.enterprise.context.spi.CreationalContext;

/**
 * This Exception is thrown if
 * {@link Context#get(jakarta.enterprise.context.spi.Contextual)} or
 * {@link Context#get(jakarta.enterprise.context.spi.Contextual, jakarta.enterprise.context.spi.CreationalContext)}
 * is called on a Context which is not 'active' in respect to the current thread.
 * This ultimately also happens if a CDI scoped Contextual Reference (the CDI proxy for a Contextual Instance)
 * of a CDI bean gets accessed in situations where it's Context is not available.
 *
 * An example of such a case would be calling a method on a &#064;SessionScoped CDI bean in a situation where
 * we do not have an active session like e.g. during an &#064;Asynchronous EJB method.
 *
 * @see Context#get(Contextual, CreationalContext)
 * @see Context#get(Contextual)
 */
public class ContextNotActiveException extends ContextException
{

    private static final long serialVersionUID = -3599813072560026919L;
    
    public ContextNotActiveException()
    {
        
    }
    
    /**
     * Creates a new exception with message.
     * 
     * @param message message
     */
    public ContextNotActiveException(String message)
    {
        super(message);
    }

    /**
     * Create a new exception with the root cause.
     * 
     * @param cause cause of the exception
     */
    public ContextNotActiveException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Creates a new exception with the given message and throwable cause.
     * 
     * @param message exception message
     * @param cause root cause of the exception
     */
    public ContextNotActiveException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
