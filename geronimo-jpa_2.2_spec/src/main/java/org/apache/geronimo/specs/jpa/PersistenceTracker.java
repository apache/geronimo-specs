/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.specs.jpa;

import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * Used to track JPA service providers in an OSGi environment.
 *
 * @version $Rev$ $Date$
 */
public class PersistenceTracker implements ServiceTrackerCustomizer {

    private PersistenceActivator activator;
    
    public PersistenceTracker(PersistenceActivator activator) {
        this.activator = activator;
    }
    
    /* (non-Javadoc)
     * @see org.osgi.util.tracker.ServiceTrackerCustomizer#addingService(org.osgi.framework.ServiceReference)
     */
    public Object addingService(ServiceReference arg0) {
        return activator.addProvider(arg0);
    }

    /* (non-Javadoc)
     * @see org.osgi.util.tracker.ServiceTrackerCustomizer#modifiedService(org.osgi.framework.ServiceReference, java.lang.Object)
     */
    public void modifiedService(ServiceReference arg0, Object arg1) {
        // handle as a remove - modify is unsupported
        removedService(arg0, arg1);
    }

    /* (non-Javadoc)
     * @see org.osgi.util.tracker.ServiceTrackerCustomizer#removedService(org.osgi.framework.ServiceReference, java.lang.Object)
     */
    public void removedService(ServiceReference arg0, Object arg1) {
        activator.removeProvider(arg0);
    }

}
