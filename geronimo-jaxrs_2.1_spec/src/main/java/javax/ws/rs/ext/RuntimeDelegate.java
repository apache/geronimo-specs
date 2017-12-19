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

package javax.ws.rs.ext;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Variant.VariantListBuilder;
import java.lang.reflect.ReflectPermission;


public abstract class RuntimeDelegate {
    public static final String JAXRS_RUNTIME_DELEGATE_PROPERTY = "javax.ws.rs.ext.RuntimeDelegate";

    private static final Object LOCK = new Object();
    private static ReflectPermission suppressAccessChecksPermission = new ReflectPermission("suppressAccessChecks");
    private static volatile RuntimeDelegate cachedDelegate;

    protected RuntimeDelegate() {
        // no-op
    }

    public static RuntimeDelegate getInstance() {
        RuntimeDelegate result = cachedDelegate;
        if (result == null) {
            synchronized (LOCK) {
                result = cachedDelegate;
                if (result == null) {
                    cachedDelegate = result = findDelegate();
                }
            }
        }
        return result;
    }


    private static RuntimeDelegate findDelegate() {
        try {
            final Object delegate = RuntimeDelegateFinder.find(JAXRS_RUNTIME_DELEGATE_PROPERTY);
            if (!RuntimeDelegate.class.isInstance(delegate)) {
                throw new LinkageError(delegate + " not an instance of RuntimeDelegate");
            }
            return RuntimeDelegate.class.cast(delegate);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public static void setInstance(final RuntimeDelegate rd) {
        final SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkPermission(suppressAccessChecksPermission);
        }
        synchronized (LOCK) {
            RuntimeDelegate.cachedDelegate = rd;
        }
    }


    public abstract UriBuilder createUriBuilder();


    public abstract ResponseBuilder createResponseBuilder();


    public abstract VariantListBuilder createVariantListBuilder();


    public abstract <T> T createEndpoint(Application application, Class<T> endpointType) throws IllegalArgumentException, UnsupportedOperationException;


    public abstract <T> HeaderDelegate<T> createHeaderDelegate(Class<T> type) throws IllegalArgumentException;


    public static interface HeaderDelegate<T> {


        public T fromString(String value);


        public String toString(T value);
    }


    public abstract Link.Builder createLinkBuilder();
}
