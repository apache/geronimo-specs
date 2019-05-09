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

package javax.ws.rs.core;

import java.security.Principal;

public interface SecurityContext {

    public static final String BASIC_AUTH = "BASIC";

    public static final String CLIENT_CERT_AUTH = "CLIENT_CERT";

    public static final String DIGEST_AUTH = "DIGEST";

    public static final String FORM_AUTH = "FORM";

    public Principal getUserPrincipal();

    public boolean isUserInRole(String role);

    public boolean isSecure();

    public String getAuthenticationScheme();
}
