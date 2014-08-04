/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.geronimo.mail;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * The activator that starts and manages the tracking of
 * JAF activation command maps
 */
public class Activator extends org.apache.geronimo.osgi.locator.Activator {
    // tracker to watch for bundle updates
    protected BundleTracker bt;
    // service tracker for a logging service
    protected ServiceTracker lst;
    // an array of all active logging services.
    protected List<LogService> logServices = new ArrayList<LogService>();

    @Override
    public synchronized void start(final BundleContext context) throws Exception {
        super.start(context);
        lst = new LogServiceTracker(context, LogService.class.getName(), null);
        lst.open();
	    bt = new BundleTracker(context, Bundle.ACTIVE, new MailProviderBundleTrackerCustomizer(this, context.getBundle()));
	    bt.open();
	}

    @Override
	public synchronized void stop(final BundleContext context) throws Exception {
	    bt.close();
	    lst.close();
        super.stop(context);
	}

	void log(final int level, final String message) {
	    synchronized (logServices) {
	        for (final LogService log : logServices) {
	            log.log(level, message);
	        }
        }
	}

	void log(final int level, final String message, final Throwable th) {
        synchronized (logServices) {
            for (final LogService log : logServices) {
                log.log(level, message, th);
            }
        }
    }

	private final class LogServiceTracker extends ServiceTracker {
        private LogServiceTracker(final BundleContext context, final String clazz,
                final ServiceTrackerCustomizer customizer) {
            super(context, clazz, customizer);
        }

        @Override
        public Object addingService(final ServiceReference reference) {
            final Object svc = super.addingService(reference);
            if (svc instanceof LogService) {
                synchronized (logServices) {
                    logServices.add((LogService) svc);
                }
            }
            return svc;
        }

        @Override
        public void removedService(final ServiceReference reference, final Object service) {
            synchronized (logServices) {
                logServices.remove(service);
            }
            super.removedService(reference, service);
        }
    }
}
