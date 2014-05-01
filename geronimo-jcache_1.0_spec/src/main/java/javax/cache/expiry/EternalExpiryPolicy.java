/*
 *
 * Apache Geronimo JCache Spec 1.0
 *
 * Copyright (C) 2003 - 2014 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package javax.cache.expiry;


import javax.cache.configuration.Factory;
import javax.cache.configuration.FactoryBuilder;
import java.io.Serializable;

import static javax.cache.expiry.Duration.ETERNAL;


public final class EternalExpiryPolicy implements ExpiryPolicy, Serializable {
    public static final long serialVersionUID = 201305101603L;

    public static Factory<ExpiryPolicy> factoryOf() {
        return new FactoryBuilder.SingletonFactory<ExpiryPolicy>(new EternalExpiryPolicy());
    }

    @Override
    public Duration getExpiryForCreation() {
        return ETERNAL;
    }

    @Override
    public Duration getExpiryForAccess() {
        return null;
    }

    @Override
    public Duration getExpiryForUpdate() {
        return null;
    }

    @Override
    public int hashCode() {
        return EternalExpiryPolicy.class.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof EternalExpiryPolicy;
    }
}
