/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


package javax.servlet;

import java.util.Collection;
import java.util.Collections;

import javax.servlet.annotation.ServletSecurity;

/**
 * @version $Rev$ $Date$
 * @since 3.0
 */
public class ServletSecurityElement extends HttpConstraintElement {

    private final Collection<HttpMethodConstraintElement> httpMethodConstraints;

    private final Collection<String> methodNames;

    public ServletSecurityElement() {
        httpMethodConstraints = Collections.emptySet();
        methodNames = Collections.emptySet();
    }

    public ServletSecurityElement(HttpConstraintElement httpConstraintElement) {
        super(httpConstraintElement.getEmptyRoleSemantic(), httpConstraintElement.getTransportGuarantee(), httpConstraintElement.getRolesAllowed());
        httpMethodConstraints = Collections.emptySet();
        methodNames = Collections.emptySet();
    }

    public ServletSecurityElement(Collection<HttpMethodConstraintElement> httpMethodConstraints) {
        this.httpMethodConstraints = httpMethodConstraints;
        this.methodNames = Collections.emptySet();
    }

    public ServletSecurityElement(HttpConstraintElement httpConstraintElement, Collection<HttpMethodConstraintElement> httpMethodConstraints) {
        super(httpConstraintElement.getEmptyRoleSemantic(), httpConstraintElement.getTransportGuarantee(), httpConstraintElement.getRolesAllowed());
        this.httpMethodConstraints = httpMethodConstraints;
        this.methodNames = Collections.emptySet();
    }

    public ServletSecurityElement(ServletSecurity servletSecurity) {
        super(servletSecurity.value().value(), servletSecurity.value().transportGuarantee(), servletSecurity.value().rolesAllowed());
        httpMethodConstraints = Collections.emptySet();
        methodNames = Collections.emptySet();
    }

    public Collection<HttpMethodConstraintElement> getHttpMethodConstraints() {
        return httpMethodConstraints;
    }

    public Collection<String> getMethodNames() {
        return methodNames;
    }
}
