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
public class OSGiLocatorTest {
    @Inject
    protected BundleContext bundleContext;

    @org.ops4j.pax.exam.junit.Configuration
    public static Option[] configuration() throws Exception {
        Option[] options = options(
            // the target code we're testing...
            mavenBundle("org.apache.geronimo.specs", "geronimo-osgi-registry"),
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
    public void testLocator() throws Exception {
        Bundle bundle1 = getInstalledBundle("org.apache.geronimo.specs.geronimo-osgi-itesta");
        // check for the target class a verify we got the correct one
        Class<?> target = ProviderLocator.locate("org.apache.geronimo.osgi.registry.itest.TestTarget");
        assertNotNull(target);
        // this should return the given class instance
        assertEquals("org.apache.geronimo.osgi.itesta.TestTarget", target.getName());

        List<Class<?>> targets = ProviderLocator.locateAll("org.apache.geronimo.osgi.registry.itest.TestTarget");
        // should return one entry and it should be the same class mapping
        assertEquals(1, targets.size());
        assertEquals("org.apache.geronimo.osgi.itesta.TestTarget", targets.get(0).getName());

        // now load the second target interface.  This is really the same as the first, but
        // uses a different commenting style.
        target = ProviderLocator.locate("org.apache.geronimo.osgi.registry.itest.TestTarget2");
        assertNotNull(target);
        // this should return the given class instance
        assertEquals("org.apache.geronimo.osgi.itesta.TestTargetTwo", target.getName());

        targets = ProviderLocator.locateAll("org.apache.geronimo.osgi.registry.itest.TestTarget2");
        // should return one entry and it should be the same class mapping
        assertEquals(1, targets.size());
        assertEquals("org.apache.geronimo.osgi.itesta.TestTargetTwo", targets.get(0).getName());

        // this class is defined using the header form.  It should map back to itself.
        target = ProviderLocator.locate("org.apache.geronimo.osgi.itesta..TestTarget2");
        assertNotNull(target);
        // this should return the given class instance
        assertEquals("org.apache.geronimo.osgi.itesta.TestTarget2", target.getName());

        targets = ProviderLocator.locateAll("org.apache.geronimo.osgi.itesta..TestTarget2");
        // should return one entry and it should be the same class mapping
        assertEquals(1, targets.size());
        assertEquals("org.apache.geronimo.osgi.itesta.TestTarget2", targets.get(0).getName());

        // This is not in the provider registry, so it should just return null.
        target = ProviderLocator.locate("org.apache.geronimo.osgi.registry.itest.ClassNotFound");
        assertNull(target);

        // This is in the provider registry, but the class cannot be loaded.  This should still
        // return null.
        target = ProviderLocator.locate("org.apache.geronimo.osgi.registry.itest.NoClass");
        assertNull(target);

        // This is defined as a header, but the class cannot be loaded.  This should still
        // return null.
        target = ProviderLocator.locate("org.apache.geronimo.osgi.itesta..NoClass");
        assertNull(target);

        // Now test some of the class loading support.  This will search the bundle classpath first,
        // then the provider registry.  Since there is a matching local class this time, it should
        // find that test class rather than the registered one.
        // check for the target class a verify we got the correct one
        target = ProviderLocator.loadClass("org.apache.geronimo.osgi.registry.itest.TestTarget", this.getClass());
        assertNotNull(target);
        // this should return the given class instance
        assertEquals(TestTarget.class.getName(), target.getName());
        // similar to the above, but since the context class is not provided, this will fail to resolve the
        // locally resident class.
        target = ProviderLocator.loadClass("org.apache.geronimo.osgi.registry.itest.TestTarget", null);
        assertNotNull(target);
        // this should return the given class instance
        assertEquals("org.apache.geronimo.osgi.itesta.TestTarget", target.getName());

        // this one should be handled from the provider registry and resolve to a class
        // defined in the test bundle.
        target = ProviderLocator.loadClass("org.apache.geronimo.osgi.registry.itest.TestTarget4", this.getClass());
        assertNotNull(target);
        // this should return the given class instance
        assertEquals("org.apache.geronimo.osgi.itesta.TestTarget", target.getName());

        // this should not be located at all
        try {
            target = ProviderLocator.loadClass("org.apache.geronimo.osgi.registry.itest.ClassNotFound", this.getClass());
            fail("Expected ClassNotFoundException not thrown");
        } catch (ClassNotFoundException e) {
        }

        // this is in the registry, but there is an error resolving the class
        try {
            target = ProviderLocator.loadClass("org.apache.geronimo.osgi.registry.itest.NoClass", this.getClass());
            fail("Expected ClassNotFoundException not thrown");
        } catch (ClassNotFoundException e) {
        }

        // this is defined with a header, but there is an error resolving the class
        try {
            target = ProviderLocator.loadClass("org.apache.geronimo.osgi.itesta.NoClass", this.getClass());
            fail("Expected ClassNotFoundException not thrown");
        } catch (ClassNotFoundException e) {
        }


        // this mapping defines multiple classes for the single key.  The first should be returned for
        // a singleton request, the locate all should return in definition order
        target = ProviderLocator.locate("org.apache.geronimo.osgi.registry.itest.TestTarget4");
        assertNotNull(target);
        // this should return the given class instance
        assertEquals("org.apache.geronimo.osgi.itesta.TestTarget", target.getName());

        targets = ProviderLocator.locateAll("org.apache.geronimo.osgi.registry.itest.TestTarget4");
        // should return one entry and it should be the same class mapping
        assertEquals(2, targets.size());
        assertEquals("org.apache.geronimo.osgi.itesta.TestTarget", targets.get(0).getName());
        assertEquals("org.apache.geronimo.osgi.itesta.TestTarget2", targets.get(1).getName());

        // now stop the first bundle, which should shuffle the deck
        bundle1.stop();

        // The returned class should now be null since there are no registered providers
        target = ProviderLocator.locate("org.apache.geronimo.osgi.registry.itest.TestTarget");
        assertNull(target);

        targets = ProviderLocator.locateAll("org.apache.geronimo.osgi.registry.itest.TestTarget");
        // should return an empty list
        assertNotNull(targets);
        assertEquals(0, targets.size());

        // The returned class should now be null since there are no registered providers
        target = ProviderLocator.locate("org.apache.geronimo.osgi.itesta.TestTarget2");
        assertNull(target);

        // This is in the provider registry, but the class cannot be loaded.  This should still
        // return null.
        target = ProviderLocator.locate("org.apache.geronimo.osgi.registry.itest.NoClass");
        assertNull(target);
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
