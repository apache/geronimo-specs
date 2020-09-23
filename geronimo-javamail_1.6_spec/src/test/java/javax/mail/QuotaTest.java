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

package javax.mail;

import junit.framework.TestCase;

/**
 * @version $Rev$ $Date$
 */
public class QuotaTest extends TestCase {

    public void testQuota() throws MessagingException {
        final Quota quota = new Quota("Fred");

        assertEquals(quota.quotaRoot, "Fred");
        assertNull(quota.resources);

        quota.setResourceLimit("Storage", 20000);

        assertNotNull(quota.resources);
        assertTrue(quota.resources.length == 1);
        assertEquals(quota.resources[0].name, "Storage");
        assertEquals(quota.resources[0].usage, 0);
        assertEquals(quota.resources[0].limit, 20000);

        quota.setResourceLimit("Storage", 30000);

        assertNotNull(quota.resources);
        assertTrue(quota.resources.length == 1);
        assertEquals(quota.resources[0].name, "Storage");
        assertEquals(quota.resources[0].usage, 0);
        assertEquals(quota.resources[0].limit, 30000);

        quota.setResourceLimit("Folders", 5);

        assertNotNull(quota.resources);
        assertTrue(quota.resources.length == 2);
        assertEquals(quota.resources[0].name, "Storage");
        assertEquals(quota.resources[0].usage, 0);
        assertEquals(quota.resources[0].limit, 30000);

        assertEquals(quota.resources[1].name, "Folders");
        assertEquals(quota.resources[1].usage, 0);
        assertEquals(quota.resources[1].limit, 5);
    }

}


