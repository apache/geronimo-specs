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

public final class TouchedExpiryPolicy implements ExpiryPolicy, Serializable {
    public static final long serialVersionUID = 201305291023L;

    private final Duration expiryDuration;

    public TouchedExpiryPolicy(final Duration expiryDuration) {
        this.expiryDuration = expiryDuration;
    }

    public static Factory<ExpiryPolicy> factoryOf(Duration duration) {
        return new FactoryBuilder.SingletonFactory<ExpiryPolicy>(new TouchedExpiryPolicy(duration));
    }

    @Override
    public Duration getExpiryForCreation() {
        return expiryDuration;
    }

    @Override
    public Duration getExpiryForAccess() {
        return expiryDuration;
    }

    @Override
    public Duration getExpiryForUpdate() {
        return expiryDuration;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((expiryDuration == null) ? 0 : expiryDuration.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof TouchedExpiryPolicy)) {
            return false;
        }
        TouchedExpiryPolicy other = (TouchedExpiryPolicy) object;
        if (expiryDuration == null) {
            if (other.expiryDuration != null) {
                return false;
            }
        } else if (!expiryDuration.equals(other.expiryDuration)) {
            return false;
        }
        return true;
    }
}
