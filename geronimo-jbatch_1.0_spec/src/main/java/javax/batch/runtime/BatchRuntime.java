/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package javax.batch.runtime;

import org.apache.geronimo.osgi.locator.ProviderLocator;

import javax.batch.operations.JobOperator;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class BatchRuntime {
    public static JobOperator getJobOperator() {
        if (System.getSecurityManager() == null) {
            return findJobOperator();
        }
        return AccessController.doPrivileged(new PrivilegedAction<JobOperator>() {
            public JobOperator run() {
                return findJobOperator();
            }
        });
    }

    private static JobOperator findJobOperator() {
        final Iterator<JobOperator> iterator = operators();
        while (iterator.hasNext()) {
            final JobOperator provider = iterator.next();
            if (provider != null) {
                return provider;
            }
        }
        return null;
    }

    private static Iterator<JobOperator> operators() {
        try {
            return ((List<JobOperator>) ProviderLocator.getService(JobOperator.class.getName(), JobOperator.class, Thread.currentThread().getContextClassLoader())).iterator();
        } catch (final Throwable th) { // rarely case where potentially missing OSGi stuff
            return ServiceLoader.load(JobOperator.class).iterator();
        }
    }
}
