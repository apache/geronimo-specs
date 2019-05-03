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
package jakarta.enterprise.event;

import java.util.concurrent.Executor;

/**
 * Used to configure ObserverMethod notifications
 */
public interface NotificationOptions
{

    /**
     * @return the Executor used for async events
     */
    Executor getExecutor();


    Object get(String optionKey);

    static NotificationOptions ofExecutor(Executor executor)
    {
        return builder().setExecutor(executor).build();
    }

    static NotificationOptions of(String optionKey, Object value)
    {
        return builder().set(optionKey, value).build();
    }

    static Builder builder()
    {
        return new ImmutableNotificationOptions.Builder();
    }


    interface Builder
    {
        Builder setExecutor(Executor executor);

        Builder set(String optionKey, Object value);

        NotificationOptions build();
    }
}
