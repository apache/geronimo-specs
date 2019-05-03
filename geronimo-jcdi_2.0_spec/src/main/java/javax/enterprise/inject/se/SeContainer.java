/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package javax.enterprise.inject.se;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;

/**
 * A representation of the SeContainer that was bootstrapped from a SeContainerInitializer
 */
public interface SeContainer extends Instance<Object>,AutoCloseable
{
    /**
     * Shuts down this SeContainer instance.  This is invoked automatically when used with a try-with-resources block
     *
     * @throws IllegalStateException if the container is already shutdown
     */
    @Override
    void close();

    /**
     * @return true if invoked before calling close(), false if it was shut down
     */
    boolean isRunning();

    /**
     * @return the BeanManager that is backing this SeContainer
     * @throws IllegalStateException if the container was already shut down
     */
    BeanManager getBeanManager();

}