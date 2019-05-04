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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;

import javax.servlet.annotation.HttpMethodConstraint;
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

    public ServletSecurityElement(HttpConstraintElement defaultHttpConstraintElement) {
        super(defaultHttpConstraintElement.getEmptyRoleSemantic(), defaultHttpConstraintElement.getTransportGuarantee(), defaultHttpConstraintElement.getRolesAllowed());
        httpMethodConstraints = Collections.emptySet();
        methodNames = Collections.emptySet();
    }

    public ServletSecurityElement(Collection<HttpMethodConstraintElement> httpMethodConstraints) throws IllegalArgumentException {
        this.httpMethodConstraints = httpMethodConstraints;
        this.methodNames = toMethodNames(httpMethodConstraints);
    }

    public ServletSecurityElement(HttpConstraintElement httpConstraintElement, Collection<HttpMethodConstraintElement> httpMethodConstraints) throws IllegalArgumentException {
        super(httpConstraintElement.getEmptyRoleSemantic(), httpConstraintElement.getTransportGuarantee(), httpConstraintElement.getRolesAllowed());
        this.httpMethodConstraints = Collections.unmodifiableCollection(httpMethodConstraints);
        this.methodNames = toMethodNames(httpMethodConstraints);
    }

    public ServletSecurityElement(ServletSecurity servletSecurity) throws IllegalArgumentException {
        super(servletSecurity.value().value(), servletSecurity.value().transportGuarantee(), servletSecurity.value().rolesAllowed());
        Collection<HttpMethodConstraintElement> httpMethodConstraints = new ArrayList<HttpMethodConstraintElement>();
        for (HttpMethodConstraint constraint: servletSecurity.httpMethodConstraints()) {
            httpMethodConstraints.add(new HttpMethodConstraintElement(constraint.value(),
                    new HttpConstraintElement(constraint.emptyRoleSemantic(),
                            constraint.transportGuarantee(),
                            constraint.rolesAllowed())));
        }
        this.httpMethodConstraints = Collections.unmodifiableCollection(httpMethodConstraints);
        methodNames = toMethodNames(httpMethodConstraints);
    }

    public Collection<HttpMethodConstraintElement> getHttpMethodConstraints() {
        return httpMethodConstraints;
    }

    public Collection<String> getMethodNames() {
        return methodNames;
    }

    private static Collection<String> toMethodNames(Collection<HttpMethodConstraintElement> constraints) throws IllegalArgumentException {
        Collection<String> methodNames = new LinkedHashSet<String>(constraints.size());
        for (HttpMethodConstraintElement constraint: constraints) {
            if (!methodNames.add(constraint.getMethodName())) {
                throw new IllegalArgumentException("duplicate method name: " + constraint.getMethodName());
            }
        }
        return Collections.unmodifiableCollection(methodNames);
    }
}
