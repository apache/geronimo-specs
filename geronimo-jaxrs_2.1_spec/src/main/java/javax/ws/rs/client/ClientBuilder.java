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

package javax.ws.rs.client;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.ws.rs.core.Configurable;
import javax.ws.rs.core.Configuration;
import java.security.KeyStore;


public abstract class ClientBuilder implements Configurable<ClientBuilder> {
    public static final String JAXRS_DEFAULT_CLIENT_BUILDER_PROPERTY = "javax.ws.rs.client.ClientBuilder";

    protected ClientBuilder() {
        // no-op
    }


    public static ClientBuilder newBuilder() {
        try {
            final Object delegate = ClientFinder.find(JAXRS_DEFAULT_CLIENT_BUILDER_PROPERTY);
            if (!ClientBuilder.class.isInstance(delegate)) {
                throw new LinkageError(delegate + " not an instance of ClientBuilder");
            }
            return ClientBuilder.class.cast(delegate);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public static Client newClient() {
        return newBuilder().build();
    }


    public static Client newClient(final Configuration configuration) {
        return newBuilder().withConfig(configuration).build();
    }


    public abstract ClientBuilder withConfig(Configuration config);


    public abstract ClientBuilder sslContext(final SSLContext sslContext);


    public abstract ClientBuilder keyStore(final KeyStore keyStore, final char[] password);


    public ClientBuilder keyStore(final KeyStore keyStore, final String password) {
        return keyStore(keyStore, password.toCharArray());
    }


    public abstract ClientBuilder trustStore(final KeyStore trustStore);


    public abstract ClientBuilder hostnameVerifier(final HostnameVerifier verifier);


    public abstract Client build();
}
