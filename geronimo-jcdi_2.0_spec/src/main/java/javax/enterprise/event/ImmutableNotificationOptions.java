/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package javax.enterprise.event;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Used to configure ObserverMethod notifications
 */
class ImmutableNotificationOptions implements NotificationOptions
{
    private final Executor executor;
    private final Map<String, Object> options;

    private ImmutableNotificationOptions(Executor executor, Map<String, Object> options)
    {
        this.executor = executor;
        this.options = options;
    }

    @Override
    public Executor getExecutor()
    {
        return executor;
    }

    @Override
    public Object get(String optionKey)
    {
        return options.get(optionKey);
    }

    static class Builder implements NotificationOptions.Builder
    {
        private Executor executor;
        private Map<String, Object> options = new HashMap<>();

        @Override
        public NotificationOptions.Builder setExecutor(Executor executor)
        {
            this.executor = executor;
            return this;
        }

        @Override
        public NotificationOptions.Builder set(String optionKey, Object value)
        {
            options.put(optionKey, value);
            return this;
        }

        @Override
        public NotificationOptions build()
        {
            return new ImmutableNotificationOptions(executor, options);
        }
    }
}
