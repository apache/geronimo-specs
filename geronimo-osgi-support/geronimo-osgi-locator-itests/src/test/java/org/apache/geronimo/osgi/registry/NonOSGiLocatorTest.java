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
package org.apache.geronimo.osgi.registry;

import java.io.InputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
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
        Class<?> target = ProviderLocator.locate("org.apache.geronimo.osgi.registry.TestTarget");
        assertNull(target);
    }
}
