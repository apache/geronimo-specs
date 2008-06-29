/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javax.availability.management;

/**
 * @version $Rev$ $Date$
 */
public class AvailabilityAgentFactory {

    private static final Object LOCK = new Object();
    private static AvailabilityAgent agent;

    public static synchronized AvailabilityAgent instantiateAvailabilityAgent() throws AvailabilityException {

        synchronized (LOCK) {

            if (agent != null) return agent;

            String agentClassName = System.getProperty("javax.availability.management.agent");

            if (agentClassName == null) throw new AvailabilityException("javax.availability.management.agent has not been set");

            try {
                Class<?> agentClass = Class.forName(agentClassName);
                agent = (AvailabilityAgent) agentClass.newInstance();
            } catch (ClassNotFoundException cnfe) {
                throw new AvailabilityException("Unable to locate class " + agentClassName, cnfe);
            } catch (InstantiationException ie) {
                throw new AvailabilityException("Unable to instantiate class " + agentClassName, ie);
            } catch (IllegalAccessException iae) {
                throw new AvailabilityException("Unable to access class " + agentClassName, iae);
            } catch (ClassCastException cce) {
                throw new AvailabilityException("The class " + agentClassName + " does not implement AvailabilityAgent", cce);
            }

            return agent;
        }
    }
}
