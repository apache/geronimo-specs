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
package javax.cache.configuration;

import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryListener;

public class MutableCacheEntryListenerConfiguration<K, V> implements CacheEntryListenerConfiguration<K, V> {
    public static final long serialVersionUID = 201403L;

    private Factory<CacheEntryListener<? super K, ? super V>> listenerFactory;

    private Factory<CacheEntryEventFilter<? super K, ? super V>> filterFactory;

    private boolean isOldValueRequired;


    private boolean isSynchronous;

    public MutableCacheEntryListenerConfiguration(final CacheEntryListenerConfiguration<K, V> configuration) {
        this.listenerFactory = configuration.getCacheEntryListenerFactory();
        this.filterFactory = configuration.getCacheEntryEventFilterFactory();
        this.isOldValueRequired = configuration.isOldValueRequired();
        this.isSynchronous = configuration.isSynchronous();
    }

    public MutableCacheEntryListenerConfiguration(final Factory<? extends CacheEntryListener<? super K, ? super V>> listenerFactory,
                                                  final Factory<? extends CacheEntryEventFilter<? super K, ? super V>> filterFactory,
                                                  final boolean isOldValueRequired,
                                                  final boolean isSynchronous) {
        this.listenerFactory = (Factory<CacheEntryListener<? super K, ? super V>>) listenerFactory;
        this.filterFactory = (Factory<CacheEntryEventFilter<? super K, ? super V>>) filterFactory;
        this.isOldValueRequired = isOldValueRequired;
        this.isSynchronous = isSynchronous;
    }


    public Factory<CacheEntryListener<? super K, ? super V>> getCacheEntryListenerFactory() {
        return listenerFactory;
    }

    public MutableCacheEntryListenerConfiguration<K, V> setCacheEntryListenerFactory(final Factory<? extends CacheEntryListener<? super K, ? super V>> listenerFactory) {
        this.listenerFactory = (Factory<CacheEntryListener<? super K, ? super V>>) listenerFactory;
        return this;
    }


    public Factory<CacheEntryEventFilter<? super K, ? super V>> getCacheEntryEventFilterFactory() {
        return filterFactory;
    }

    public MutableCacheEntryListenerConfiguration<K, V> setCacheEntryEventFilterFactory(final Factory<? extends CacheEntryEventFilter<? super K, ? super V>> filterFactory) {
        this.filterFactory = (Factory<CacheEntryEventFilter<? super K, ? super V>>) filterFactory;
        return this;
    }


    public boolean isOldValueRequired() {
        return isOldValueRequired;
    }

    public MutableCacheEntryListenerConfiguration<K, V> setOldValueRequired(final boolean isOldValueRequired) {
        this.isOldValueRequired = isOldValueRequired;
        return this;
    }


    public boolean isSynchronous() {
        return isSynchronous;
    }

    public MutableCacheEntryListenerConfiguration<K, V> setSynchronous(final boolean isSynchronous) {
        this.isSynchronous = isSynchronous;
        return this;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((filterFactory == null) ? 0 : filterFactory.hashCode());
        result = prime * result + (isOldValueRequired ? 1231 : 1237);
        result = prime * result + (isSynchronous ? 1231 : 1237);
        result = prime * result + ((listenerFactory == null) ? 0 : listenerFactory.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof MutableCacheEntryListenerConfiguration)) {
            return false;
        }
        final MutableCacheEntryListenerConfiguration<?, ?> other = (MutableCacheEntryListenerConfiguration<?, ?>) object;
        if (filterFactory == null) {
            if (other.filterFactory != null) {
                return false;
            }
        } else if (!filterFactory.equals(other.filterFactory)) {
            return false;
        }
        if (isOldValueRequired != other.isOldValueRequired) {
            return false;
        }
        if (isSynchronous != other.isSynchronous) {
            return false;
        }
        if (listenerFactory == null) {
            if (other.listenerFactory != null) {
                return false;
            }
        } else if (!listenerFactory.equals(other.listenerFactory)) {
            return false;
        }
        return true;
    }
}
