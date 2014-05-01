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


package javax.cache.spi;


import javax.cache.CacheManager;
import javax.cache.configuration.OptionalFeature;
import java.io.Closeable;
import java.net.URI;
import java.util.Properties;


public interface CachingProvider extends Closeable {
    CacheManager getCacheManager(URI uri, ClassLoader classLoader, Properties properties);


    ClassLoader getDefaultClassLoader();


    URI getDefaultURI();


    Properties getDefaultProperties();


    CacheManager getCacheManager(URI uri, ClassLoader classLoader);


    CacheManager getCacheManager();


    void close();


    void close(ClassLoader classLoader);


    void close(URI uri, ClassLoader classLoader);


    boolean isSupported(OptionalFeature optionalFeature);
}
