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
package javax.cache;

import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;
import java.io.Closeable;
import java.net.URI;
import java.util.Properties;

public interface CacheManager extends Closeable {
    CachingProvider getCachingProvider();


    URI getURI();


    ClassLoader getClassLoader();


    Properties getProperties();


    <K, V, C extends Configuration<K, V>> Cache<K, V> createCache(String cacheName,
                                                                  C configuration)
            throws IllegalArgumentException;


    <K, V> Cache<K, V> getCache(String cacheName, Class<K> keyType,
                                Class<V> valueType);


    <K, V> Cache<K, V> getCache(String cacheName);


    Iterable<String> getCacheNames();


    void destroyCache(String cacheName);


    void enableManagement(String cacheName, boolean enabled);


    void enableStatistics(String cacheName, boolean enabled);


    void close();


    boolean isClosed();


    <T> T unwrap(java.lang.Class<T> clazz);
}
