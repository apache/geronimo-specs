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

//
// This source code implements specifications defined by the Java
// Community Process. In order to remain compliant with the specification
// DO NOT add / change / or delete method signatures!
//

package javax.resource.spi;

import java.io.PrintWriter;
import javax.resource.ResourceException;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;

/**
 * @version $Rev$ $Date$
 */
public interface ManagedConnection {
    Object getConnection(Subject subject, ConnectionRequestInfo cxRequestInfo) throws ResourceException;

    void destroy() throws ResourceException;

    void cleanup() throws ResourceException;

    void associateConnection(Object connection) throws ResourceException;

    void addConnectionEventListener(ConnectionEventListener listener);

    void removeConnectionEventListener(ConnectionEventListener listener);

    XAResource getXAResource() throws ResourceException;

    LocalTransaction getLocalTransaction() throws ResourceException;

    ManagedConnectionMetaData getMetaData() throws ResourceException;

    void setLogWriter(PrintWriter out) throws ResourceException;

    PrintWriter getLogWriter() throws ResourceException;
}