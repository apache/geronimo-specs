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
package javax.util.concurrent;

import java.util.Locale;

/**
 * A tasks submitted to an {@link javax.util.concurrent.ManagedExecutorService}
 * can optionally implement this interface.<p>
 *
 * The intent of this interface is to allow management facilities to inspect the task to determine
 * the intent of the task and its state.  Implementations should not depend upon any thread execution
 * context and should typically return only readily-available instance data.
 */
public interface Identifiable {

    /**
     * The name or ID of the identifiable object.
     * @return the name or ID of the identifiable object.
     */
    public String getIdentityName();

    /**
     * The description of the identifiable object translated for a given locale.
     * @param locale the locale to use to generate the description.  If null, the default locale will be
     * used.
     * @return the description of the identifiable object  in the specified locale.
     */
    public String getIdentityDescription(Locale locale);

}
