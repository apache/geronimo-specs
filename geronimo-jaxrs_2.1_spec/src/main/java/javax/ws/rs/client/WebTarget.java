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
import java.util.Map;

import javax.ws.rs.core.Configurable;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

public interface WebTarget extends Configurable<WebTarget> {

    public URI getUri();

    public UriBuilder getUriBuilder();

    public WebTarget path(String path);

    public WebTarget resolveTemplate(String name, Object value);

    public WebTarget resolveTemplate(String name, Object value, boolean encodeSlashInPath);

    public WebTarget resolveTemplateFromEncoded(String name, Object value);

    public WebTarget resolveTemplates(Map<String, Object> templateValues);

    public WebTarget resolveTemplates(Map<String, Object> templateValues, boolean encodeSlashInPath);

    public WebTarget resolveTemplatesFromEncoded(Map<String, Object> templateValues);

    public WebTarget matrixParam(String name, Object... values);

    public WebTarget queryParam(String name, Object... values);

    public Invocation.Builder request();

    public Invocation.Builder request(String... acceptedResponseTypes);

    public Invocation.Builder request(MediaType... acceptedResponseTypes);
}
