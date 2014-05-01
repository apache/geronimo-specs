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

import javax.cache.expiry.EternalExpiryPolicy;
import javax.cache.expiry.ExpiryPolicy;
import javax.cache.integration.CacheLoader;
import javax.cache.integration.CacheWriter;
import java.util.HashSet;

public class MutableConfiguration<K, V> implements CompleteConfiguration<K, V> {
    public static final long serialVersionUID = 201405L;

    protected Class<K> keyType;
    protected Class<V> valueType;
    protected HashSet<CacheEntryListenerConfiguration<K, V>> listenerConfigurations;
    protected Factory<CacheLoader<K, V>> cacheLoaderFactory;
    protected Factory<CacheWriter<? super K, ? super V>> cacheWriterFactory;
    protected Factory<ExpiryPolicy> expiryPolicyFactory;
    protected boolean isReadThrough;
    protected boolean isWriteThrough;
    protected boolean isStatisticsEnabled;
    protected boolean isStoreByValue;
    protected boolean isManagementEnabled;

    public MutableConfiguration() {
        this.keyType = (Class<K>) Object.class;
        this.valueType = (Class<V>) Object.class;
        this.listenerConfigurations = new HashSet<CacheEntryListenerConfiguration<K, V>>();
        this.cacheLoaderFactory = null;
        this.cacheWriterFactory = null;
        this.expiryPolicyFactory = EternalExpiryPolicy.factoryOf();
        this.isReadThrough = false;
        this.isWriteThrough = false;
        this.isStatisticsEnabled = false;
        this.isStoreByValue = true;
        this.isManagementEnabled = false;
    }

    public MutableConfiguration(CompleteConfiguration<K, V> configuration) {

        this.keyType = configuration.getKeyType();
        this.valueType = configuration.getValueType();

        listenerConfigurations = new HashSet<CacheEntryListenerConfiguration<K, V>>();
        for (final CacheEntryListenerConfiguration<K, V> definition : configuration.getCacheEntryListenerConfigurations()) {
            addCacheEntryListenerConfiguration(definition);
        }

        this.cacheLoaderFactory = configuration.getCacheLoaderFactory();
        this.cacheWriterFactory = configuration.getCacheWriterFactory();

        if (configuration.getExpiryPolicyFactory() == null) {
            this.expiryPolicyFactory = EternalExpiryPolicy.factoryOf();
        } else {
            this.expiryPolicyFactory = configuration.getExpiryPolicyFactory();
        }

        this.isReadThrough = configuration.isReadThrough();
        this.isWriteThrough = configuration.isWriteThrough();

        this.isStatisticsEnabled = configuration.isStatisticsEnabled();

        this.isStoreByValue = configuration.isStoreByValue();

        this.isManagementEnabled = configuration.isManagementEnabled();
    }

    @Override
    public Class<K> getKeyType() {
        return keyType;
    }

    @Override
    public Class<V> getValueType() {
        return valueType;
    }

    public MutableConfiguration<K, V> setTypes(final Class<K> keyType, final Class<V> valueType) {
        if (keyType == null || valueType == null) {
            throw new NullPointerException("keyType and/or valueType can't be null");
        } else {
            this.keyType = keyType;
            this.valueType = valueType;
            return this;
        }
    }

    @Override
    public Iterable<CacheEntryListenerConfiguration<K, V>> getCacheEntryListenerConfigurations() {
        return listenerConfigurations;
    }

    public MutableConfiguration<K, V> addCacheEntryListenerConfiguration(final CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration) {
        if (cacheEntryListenerConfiguration == null) {
            throw new NullPointerException("CacheEntryListenerConfiguration can't be null");
        }

        boolean alreadyExists = false;
        for (final CacheEntryListenerConfiguration<? super K, ? super V> c : listenerConfigurations) {
            if (c.equals(cacheEntryListenerConfiguration)) {
                alreadyExists = true;
            }
        }

        if (!alreadyExists) {
            this.listenerConfigurations.add(cacheEntryListenerConfiguration);
        } else {
            throw new IllegalArgumentException("A CacheEntryListenerConfiguration can " +
                    "be registered only once");
        }
        return this;
    }


    public MutableConfiguration<K, V> removeCacheEntryListenerConfiguration(final CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration) {
        if (cacheEntryListenerConfiguration == null) {
            throw new NullPointerException("CacheEntryListenerConfiguration can't be null");
        }
        listenerConfigurations.remove(cacheEntryListenerConfiguration);
        return this;
    }

    @Override
    public Factory<CacheLoader<K, V>> getCacheLoaderFactory() {
        return this.cacheLoaderFactory;
    }

    public MutableConfiguration<K, V> setCacheLoaderFactory(final Factory<? extends CacheLoader<K, V>> factory) {
        this.cacheLoaderFactory = (Factory<CacheLoader<K, V>>) factory;
        return this;
    }

    @Override
    public Factory<CacheWriter<? super K, ? super V>> getCacheWriterFactory() {
        return this.cacheWriterFactory;
    }

    public MutableConfiguration<K, V> setCacheWriterFactory(final Factory<? extends
            CacheWriter<? super K, ? super V>> factory) {
        this.cacheWriterFactory = (Factory<CacheWriter<? super K, ? super V>>) factory;
        return this;
    }

    public Factory<ExpiryPolicy> getExpiryPolicyFactory() {
        return this.expiryPolicyFactory;
    }

    public MutableConfiguration<K, V> setExpiryPolicyFactory(final Factory<? extends ExpiryPolicy> factory) {
        if (factory == null) {
            this.expiryPolicyFactory = EternalExpiryPolicy.factoryOf();
        } else {
            this.expiryPolicyFactory = (Factory<ExpiryPolicy>) factory;
        }
        return this;
    }

    @Override
    public boolean isReadThrough() {
        return this.isReadThrough;
    }

    public MutableConfiguration<K, V> setReadThrough(final boolean isReadThrough) {
        this.isReadThrough = isReadThrough;
        return this;
    }

    @Override
    public boolean isWriteThrough() {
        return this.isWriteThrough;
    }

    public MutableConfiguration<K, V> setWriteThrough(final boolean isWriteThrough) {
        this.isWriteThrough = isWriteThrough;
        return this;
    }

    @Override
    public boolean isStoreByValue() {
        return this.isStoreByValue;
    }

    public MutableConfiguration<K, V> setStoreByValue(final boolean isStoreByValue) {
        this.isStoreByValue = isStoreByValue;
        return this;
    }

    @Override
    public boolean isStatisticsEnabled() {
        return this.isStatisticsEnabled;
    }


    public MutableConfiguration<K, V> setStatisticsEnabled(final boolean enabled) {
        this.isStatisticsEnabled = enabled;
        return this;
    }

    @Override
    public boolean isManagementEnabled() {
        return this.isManagementEnabled;
    }

    public MutableConfiguration<K, V> setManagementEnabled(final boolean enabled) {
        this.isManagementEnabled = enabled;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + keyType.hashCode();
        result = prime * result + valueType.hashCode();
        result = prime * result + ((listenerConfigurations == null) ? 0 : listenerConfigurations.hashCode());
        result = prime * result + ((cacheLoaderFactory == null) ? 0 : cacheLoaderFactory.hashCode());
        result = prime * result + ((cacheWriterFactory == null) ? 0 : cacheWriterFactory.hashCode());
        result = prime * result + ((expiryPolicyFactory == null) ? 0 : expiryPolicyFactory.hashCode());
        result = prime * result + (isReadThrough ? 1231 : 1237);
        result = prime * result + (isStatisticsEnabled ? 1231 : 1237);
        result = prime * result + (isStoreByValue ? 1231 : 1237);
        result = prime * result + (isWriteThrough ? 1231 : 1237);
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
        if (!(object instanceof MutableConfiguration)) {
            return false;
        }
        final MutableConfiguration<?, ?> other = (MutableConfiguration<?, ?>) object;
        if (!keyType.equals(other.keyType)) {
            return false;
        }
        if (!valueType.equals(other.valueType)) {
            return false;
        }
        if (!listenerConfigurations.equals(other.listenerConfigurations)) {
            return false;
        }
        if (cacheLoaderFactory == null) {
            if (other.cacheLoaderFactory != null) {
                return false;
            }
        } else if (!cacheLoaderFactory.equals(other.cacheLoaderFactory)) {
            return false;
        }
        if (cacheWriterFactory == null) {
            if (other.cacheWriterFactory != null) {
                return false;
            }
        } else if (!cacheWriterFactory.equals(other.cacheWriterFactory)) {
            return false;
        }
        if (expiryPolicyFactory == null) {
            if (other.expiryPolicyFactory != null) {
                return false;
            }
        } else if (!expiryPolicyFactory.equals(other.expiryPolicyFactory)) {
            return false;
        }
        if (isReadThrough != other.isReadThrough) {
            return false;
        }
        if (isStatisticsEnabled != other.isStatisticsEnabled) {
            return false;
        }
        if (isStoreByValue != other.isStoreByValue) {
            return false;
        }
        if (isWriteThrough != other.isWriteThrough) {
            return false;
        }
        return true;
    }
}
