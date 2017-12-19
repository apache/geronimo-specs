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

package javax.ws.rs.client;

import java.net.URI;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.ws.rs.core.Configurable;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriBuilder;

public interface Client extends Configurable<Client> {

    public void close();

    public WebTarget target(String uri);

    public WebTarget target(URI uri);

    public WebTarget target(UriBuilder uriBuilder);

    public WebTarget target(Link link);

    public Invocation.Builder invocation(Link link);

    public SSLContext getSslContext();

    public HostnameVerifier getHostnameVerifier();
}
