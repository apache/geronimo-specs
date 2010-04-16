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
public class OSGiNoRegistryTest {
    @Inject
    protected BundleContext bundleContext;

    @org.ops4j.pax.exam.junit.Configuration
    public static Option[] configuration() throws Exception {
        Option[] options = options(
            // NOTE:  This test is supposed to test that the ProviderLocator
            // does not raise exceptions if the provider registry is not
            // available and the ProviderRegistry api class is not resolvable.
//          mavenBundle("org.apache.geronimo.specs", "geronimo-osgi-registry"),
            // bundle containing test resources
            mavenBundle("org.apache.geronimo.specs", "geronimo-osgi-itesta"),
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
                        // we need an import for activator to function properly...make sure the resolution
                        // is optional, which is the typical way of dealing with this
                        .set(Constants.IMPORT_PACKAGE, "org.apache.geronimo.osgi.registry.api;resolution:=optional")
                        .build();
                }
            }
        );
        options = updateOptions(options);
        return options;
    }


    @Test
    public void testLocator() throws Exception {
        // Ok, just a few quick tests to make sure things fail gracefully.
        Class<?> target = ProviderLocator.locate("org.apache.geronimo.osgi.registry.itest.TestTarget");
        assertNull(target);

        List<Class<?>> targets = ProviderLocator.locateAll("org.apache.geronimo.osgi.registry.itest.TestTarget");
        // should return an empty list
        assertNotNull(targets);
        assertEquals(0, targets.size());

        // this should not be located at all
        try {
            target = ProviderLocator.loadClass("org.apache.geronimo.osgi.registry.itest.ClassNotFound", this.getClass());
            fail("Expected ClassNotFoundException not thrown");
        } catch (ClassNotFoundException e) {
        }

        // now some service tests
        Object service = ProviderLocator.getService("org.apache.geronimo.osgi.registry.itest.TestTarget", this.getClass(), Thread.currentThread().getContextClassLoader());
        assertNull(service);

        // now testing a multiple instances get.  This should only pick up the definition from the
        // bundle that includes the SPI-Provider header (testa)
        List services = ProviderLocator.getServices("org.apache.geronimo.osgi.registry.itest.TestTarget", this.getClass(), Thread.currentThread().getContextClassLoader());
        assertNotNull(services);
        assertEquals(0, services.size());

        // same set of tests, but this time we're looking for classes, not instances
        target = ProviderLocator.getServiceClass("org.apache.geronimo.osgi.registry.itest.TestTarget", this.getClass(), Thread.currentThread().getContextClassLoader());
        assertNull(target);

        List<Class<?>> classes = ProviderLocator.getServiceClasses("org.apache.geronimo.osgi.registry.itest.TestTarget", this.getClass(), Thread.currentThread().getContextClassLoader());
        assertNotNull(classes);
        assertEquals(0, classes.size());
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
