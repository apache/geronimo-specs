/*
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
package org.apache.geronimo.osgi.locator.itest;

import java.io.InputStream;
import java.io.IOException;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

import org.apache.geronimo.osgi.locator.ProviderLocator;

public class NonOSGiLocatorTest {
    @Test
    public void testLocator() throws Exception {
        // this is verifying that the ProviderLocater can be called when used outside of
        // an OSGi framework without causing an exception.  This will verify that the OSGi
        // classes are not on the classpath before attempting this.
        try {
            this.getClass().getClassLoader().loadClass("org.osgi.util.tracker.ServiceTracker");
            fail("OSGi framework classes must not be on the class path for this test");
        } catch (ClassNotFoundException e) {
            // this should happen if the test is run correctly.
        }

        // Run outside of an OSGi framework, this should just return null without
        // causing an error
        Class<?> target = ProviderLocator.locate("org.apache.geronimo.osgi.locator.itest.TestTarget");
        assertNull(target);

        // this should be located on our local classpath, so it should get a good return
        target = ProviderLocator.loadClass("org.apache.geronimo.osgi.locator.itest.TestTarget");
        assertNotNull(target);
        // this should return the given class instance
        assertEquals("org.apache.geronimo.osgi.locator.itest.TestTarget", target.getName());

        // this will be on our dependency class path, so it should still be located.
        target = ProviderLocator.loadClass("org.apache.geronimo.osgi.itesta.TestTarget");
        assertNotNull(target);
        // this should return the given class instance
        assertEquals("org.apache.geronimo.osgi.itesta.TestTarget", target.getName());


        // this should not be located at all
        try {
            target = ProviderLocator.loadClass("org.apache.geronimo.osgi.registry.ClassNotFound");
            fail("Expected ClassNotFoundException not thrown");
        } catch (ClassNotFoundException e) {
        }

        // now testing the services lookup and instantiation mechanism
        Object service = ProviderLocator.getService("org.apache.geronimo.osgi.registry.itest.TestTarget", this.getClass(), Thread.currentThread().getContextClassLoader());
        assertNotNull(service);
        // this should return an instance created from the services definition
        assertEquals("org.apache.geronimo.osgi.itesta.TestTarget2", service.getClass().getName());

        // we expect a new instance on each call.  Verify that the instances are different
        Object service2 = ProviderLocator.getService("org.apache.geronimo.osgi.registry.itest.TestTarget", this.getClass(), Thread.currentThread().getContextClassLoader());
        assertNotNull(service2);
        // this should return an instance created from the services definition
        assertEquals("org.apache.geronimo.osgi.itesta.TestTarget2", service2.getClass().getName());
        assertNotSame(service, service2);

        // now testing a multiple instances get.  This should pick up a definition from both
        // jars on the class path
        List services = ProviderLocator.getServices("org.apache.geronimo.osgi.registry.itest.TestTarget", this.getClass(), Thread.currentThread().getContextClassLoader());
        assertNotNull(services);
        assertEquals(2, services.size());
        // this should return an instance created from the services definition
        assertEquals("org.apache.geronimo.osgi.itesta.TestTarget2", services.get(0).getClass().getName());
        assertEquals("org.apache.geronimo.osgi.itestb.TestTarget2", services.get(1).getClass().getName());
        assertNotSame(service, services.get(0));

        // this is multiple instances defined in a single services file.
        services = ProviderLocator.getServices("org.apache.geronimo.osgi.registry.itest.MultiTarget", this.getClass(), Thread.currentThread().getContextClassLoader());
        assertNotNull(services);
        assertEquals(2, services.size());
        // this should return an instance created from the services definition
        assertEquals("org.apache.geronimo.osgi.itestb.TestTarget", services.get(0).getClass().getName());
        assertEquals("org.apache.geronimo.osgi.itestb.TestTarget2", services.get(1).getClass().getName());

        // this should not be found
        service = ProviderLocator.getService("org.apache.geronimo.osgi.registry.itest.NotFound", this.getClass(), Thread.currentThread().getContextClassLoader());
        assertNull(service);

        // again, not found.  Should return an empty list
        services = ProviderLocator.getServices("org.apache.geronimo.osgi.registry.itest.NotFound", this.getClass(), Thread.currentThread().getContextClassLoader());
        assertNotNull(services);
        assertEquals(0, services.size());

        // this should result in an exception
        try {
            service = ProviderLocator.getService("org.apache.geronimo.osgi.registry.itest.NoClass", this.getClass(), Thread.currentThread().getContextClassLoader());
            fail("Expected ClassNotFoundException not thrown");
        } catch (ClassNotFoundException e) {
        }

        // as should this
        try {
            services = ProviderLocator.getServices("org.apache.geronimo.osgi.registry.itest.NoClass", this.getClass(), Thread.currentThread().getContextClassLoader());
            fail("Expected ClassNotFoundException not thrown");
        } catch (ClassNotFoundException e) {
        }

        // this should result in an exception
        try {
            service = ProviderLocator.getService("org.apache.geronimo.osgi.registry.itest.BadClass", this.getClass(), Thread.currentThread().getContextClassLoader());
            fail("Expected Exception not thrown");
        } catch (NullPointerException e) {
        }

        // as should this
        try {
            services = ProviderLocator.getServices("org.apache.geronimo.osgi.registry.itest.BadClass", this.getClass(), Thread.currentThread().getContextClassLoader());
            fail("Expected Exception not thrown");
        } catch (NullPointerException e) {
        }

        // this should result in an exception
        try {
            service = ProviderLocator.getService("org.apache.geronimo.osgi.registry.itest.NoConstructor", this.getClass(), Thread.currentThread().getContextClassLoader());
            fail("Expected Exception not thrown");
        } catch (InstantiationException e) {
        }

        // as should this
        try {
            services = ProviderLocator.getServices("org.apache.geronimo.osgi.registry.itest.NoConstructor", this.getClass(), Thread.currentThread().getContextClassLoader());
            fail("Expected Exception not thrown");
        } catch (InstantiationException e) {
        }

        // this should result in an exception
        try {
            service = ProviderLocator.getService("org.apache.geronimo.osgi.registry.itest.NoAccess", this.getClass(), Thread.currentThread().getContextClassLoader());
            fail("Expected Exception not thrown");
        } catch (IllegalAccessException e) {
        }

        // as should this
        try {
            services = ProviderLocator.getServices("org.apache.geronimo.osgi.registry.itest.NoAccess", this.getClass(), Thread.currentThread().getContextClassLoader());
            fail("Expected Exception not thrown");
        } catch (IllegalAccessException e) {
        }

        // same set of tests, but this time we're looking for classes, not instances
        target = ProviderLocator.getServiceClass("org.apache.geronimo.osgi.registry.itest.TestTarget", this.getClass(), Thread.currentThread().getContextClassLoader());
        assertNotNull(target);
        // this should return an instance created from the services definition
        assertEquals("org.apache.geronimo.osgi.itesta.TestTarget2", target.getName());

        // now testing a multiple instances get.  This should pick up a definition from both
        // jars on the class path
        List<Class<?>> classes = ProviderLocator.getServiceClasses("org.apache.geronimo.osgi.registry.itest.TestTarget", this.getClass(), Thread.currentThread().getContextClassLoader());
        assertNotNull(classes);
        assertEquals(2, classes.size());
        // this should return an instance created from the services definition
        assertEquals("org.apache.geronimo.osgi.itesta.TestTarget2", classes.get(0).getName());
        assertEquals("org.apache.geronimo.osgi.itestb.TestTarget2", classes.get(1).getName());

        // this is multiple instances defined in a single services file.
        classes = ProviderLocator.getServiceClasses("org.apache.geronimo.osgi.registry.itest.MultiTarget", this.getClass(), Thread.currentThread().getContextClassLoader());
        assertNotNull(classes);
        assertEquals(2, classes.size());
        // this should return an instance created from the services definition
        assertEquals("org.apache.geronimo.osgi.itestb.TestTarget", classes.get(0).getName());
        assertEquals("org.apache.geronimo.osgi.itestb.TestTarget2", classes.get(1).getName());

        // this should not be found
        target = ProviderLocator.getServiceClass("org.apache.geronimo.osgi.registry.itest.NotFound", this.getClass(), Thread.currentThread().getContextClassLoader());
        assertNull(target);

        // again, not found.  Should return an empty list
        classes = ProviderLocator.getServiceClasses("org.apache.geronimo.osgi.registry.itest.NotFound", this.getClass(), Thread.currentThread().getContextClassLoader());
        assertNotNull(classes);
        assertEquals(0, classes.size());

        // this should result in an exception
        try {
            target = ProviderLocator.getServiceClass("org.apache.geronimo.osgi.registry.itest.NoClass", this.getClass(), Thread.currentThread().getContextClassLoader());
            fail("Expected ClassNotFoundException not thrown");
        } catch (ClassNotFoundException e) {
        }

        // as should this
        try {
            classes = ProviderLocator.getServiceClasses("org.apache.geronimo.osgi.registry.itest.NoClass", this.getClass(), Thread.currentThread().getContextClassLoader());
            fail("Expected ClassNotFoundException not thrown");
        } catch (ClassNotFoundException e) {
        }
    }
}
