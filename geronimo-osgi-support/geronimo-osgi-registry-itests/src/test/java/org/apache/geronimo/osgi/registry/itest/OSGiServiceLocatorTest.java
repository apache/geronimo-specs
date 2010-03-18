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
package org.apache.geronimo.osgi.registry.itest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.geronimo.osgi.locator.Activator;
import org.apache.geronimo.osgi.locator.ProviderLocator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.CoreOptions;
import static org.ops4j.pax.exam.CoreOptions.bundle;
import static org.ops4j.pax.exam.CoreOptions.equinox;
import static org.ops4j.pax.exam.CoreOptions.felix;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.provision;
import static org.ops4j.pax.exam.CoreOptions.wrappedBundle;
import org.ops4j.pax.exam.Customizer;
import org.ops4j.pax.exam.Inject;
import org.ops4j.pax.exam.Option;
import static org.ops4j.pax.exam.OptionUtils.combine;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.options.MavenArtifactProvisionOption;
import static org.ops4j.pax.swissbox.tinybundles.core.TinyBundles.*;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

@RunWith(JUnit4TestRunner.class)
public class OSGiServiceLocatorTest {
    @Inject
    protected BundleContext bundleContext;

    @org.ops4j.pax.exam.junit.Configuration
    public static Option[] configuration() throws Exception {
        Option[] options = options(
            // the target code we're testing...
            mavenBundle("org.apache.geronimo.specs", "geronimo-osgi-registry"),
            // bundle containing test resources
            mavenBundle("org.apache.geronimo.specs", "geronimo-osgi-itesta"),
            // this bundle opts out of the SPI-Provider status
            mavenBundle("org.apache.geronimo.specs", "geronimo-osgi-itestb"),
            mavenBundle("org.ops4j.pax.logging", "pax-logging-api"),
            felix(),
            equinox().version("3.5.0"),
            // we want to specify an activator for the test probe bundle that
            // is the standard one for adding the locator service to a spec
            // bundle.  We'll use our activator instance to perform class lookups
            new Customizer()
            {
                @Override
                public InputStream customizeTestProbe( InputStream testProbe )
                    throws IOException
                {
                    return modifyBundle(testProbe)
                        // these two classes need to be in every participating bundle
                        .add(org.apache.geronimo.osgi.locator.Activator.class)
                        .add(org.apache.geronimo.osgi.locator.ProviderLocator.class)
                        // we don't have any direct references to this class, so force it to be
                        // included.
                        .add(org.apache.geronimo.osgi.registry.itest.TestTargetLocal.class)
                        // set the required activator also
                        .set(Constants.BUNDLE_ACTIVATOR, org.apache.geronimo.osgi.locator.Activator.class.getName())
                        // we need an import for activator to function properly.
                        .set(Constants.IMPORT_PACKAGE, "org.apache.geronimo.osgi.registry.api")
                        .build();
                }
            }
        );
        options = updateOptions(options);
        return options;
    }


    @Test
    public void testServiceLocator() throws Exception {
        Bundle bundle1 = getInstalledBundle("org.apache.geronimo.specs.geronimo-osgi-itesta");

        // now testing the services lookup and instantiation mechanism.  These should all be satisfied by
        // the extender rather than loaded from the classpath.
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

        // now testing a multiple instances get.  This should only pick up the definition from the
        // bundle that includes the SPI-Provider header (testa)
        List services = ProviderLocator.getServices("org.apache.geronimo.osgi.registry.itest.TestTarget", this.getClass(), Thread.currentThread().getContextClassLoader());
        assertNotNull(services);
        assertEquals(1, services.size());
        // this should return an instance created from the services definition
        assertEquals("org.apache.geronimo.osgi.itesta.TestTarget2", services.get(0).getClass().getName());
        // these should be different instances
        assertNotSame(service, services.get(0));

        // this is multiple instances defined in a single services file.
        services = ProviderLocator.getServices("org.apache.geronimo.osgi.registry.itesta.MultiTarget", this.getClass(), Thread.currentThread().getContextClassLoader());
        assertNotNull(services);
        assertEquals(2, services.size());
        // this should return an instance created from the services definition
        assertEquals("org.apache.geronimo.osgi.itesta.TestTarget", services.get(0).getClass().getName());
        assertEquals("org.apache.geronimo.osgi.itesta.TestTarget2", services.get(1).getClass().getName());

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
        Class<?> target = ProviderLocator.getServiceClass("org.apache.geronimo.osgi.registry.itest.TestTarget", this.getClass(), Thread.currentThread().getContextClassLoader());
        assertNotNull(target);
        // this should return an instance created from the services definition
        assertEquals("org.apache.geronimo.osgi.itesta.TestTarget2", target.getName());

        // now testing a multiple instances get.  This should pick up a definition from both
        // jars on the class path
        List<Class<?>> classes = ProviderLocator.getServiceClasses("org.apache.geronimo.osgi.registry.itest.TestTarget", this.getClass(), Thread.currentThread().getContextClassLoader());
        assertNotNull(classes);
        assertEquals(1, classes.size());
        // this should return an instance created from the services definition
        assertEquals("org.apache.geronimo.osgi.itesta.TestTarget2", classes.get(0).getName());

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

        // now stop the first bundle, which should remove the service defintions.
        bundle1.stop();

        // repeat the first queries, which should fail now
        service = ProviderLocator.getService("org.apache.geronimo.osgi.registry.itest.TestTarget", this.getClass(), Thread.currentThread().getContextClassLoader());
        assertNull(service);

        // this should revert to an empty list now.
        services = ProviderLocator.getServices("org.apache.geronimo.osgi.registry.itest.TestTarget", this.getClass(), Thread.currentThread().getContextClassLoader());
        assertNotNull(services);
        assertEquals(0, services.size());
    }

    protected Bundle getInstalledBundle(String symbolicName) {
        for (Bundle b : bundleContext.getBundles()) {
            if (b.getSymbolicName().equals(symbolicName)) {
                return b;
            }
        }
        return null;
    }

    public static MavenArtifactProvisionOption mavenBundle(String groupId, String artifactId) {
        return CoreOptions.mavenBundle().groupId(groupId).artifactId(artifactId).versionAsInProject();
    }

    protected static Option[] updateOptions(Option[] options) {
        // We need to add pax-exam-junit here when running with the ibm
        // jdk to avoid the following exception during the test run:
        // ClassNotFoundException: org.ops4j.pax.exam.junit.Configuration
        if ("IBM Corporation".equals(System.getProperty("java.vendor"))) {
            Option[] ibmOptions = options(
                wrappedBundle(mavenBundle("org.ops4j.pax.exam", "pax-exam-junit"))
            );
            options = combine(ibmOptions, options);
        }

        return options;
    }
}
