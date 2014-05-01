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

import javax.cache.spi.CachingProvider;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ServiceLoader;
import java.util.WeakHashMap;

public final class Caching {
    public static final String JAVAX_CACHE_CACHING_PROVIDER = "javax.cache.spi.CachingProvider";

    private static final CachingProviderRegistry CACHING_PROVIDERS = new CachingProviderRegistry();

    private Caching() {
        // no-op
    }

    public static ClassLoader getDefaultClassLoader() {
        return CACHING_PROVIDERS.getDefaultClassLoader();
    }

    public static void setDefaultClassLoader(ClassLoader classLoader) {
        CACHING_PROVIDERS.setDefaultClassLoader(classLoader);
    }

    public static CachingProvider getCachingProvider() {
        return CACHING_PROVIDERS.getCachingProvider();
    }

    public static CachingProvider getCachingProvider(ClassLoader classLoader) {
        return CACHING_PROVIDERS.getCachingProvider(classLoader);
    }

    public static Iterable<CachingProvider> getCachingProviders() {
        return CACHING_PROVIDERS.getCachingProviders();
    }

    public static Iterable<CachingProvider> getCachingProviders(ClassLoader classLoader) {
        return CACHING_PROVIDERS.getCachingProviders(classLoader);
    }

    public static CachingProvider getCachingProvider(String fullyQualifiedClassName) {
        return CACHING_PROVIDERS.getCachingProvider(fullyQualifiedClassName);
    }

    public static CachingProvider getCachingProvider(String fullyQualifiedClassName, ClassLoader classLoader) {
        return CACHING_PROVIDERS.getCachingProvider(fullyQualifiedClassName, classLoader);
    }

    public static <K, V> Cache<K, V> getCache(String cacheName, Class<K> keyType, Class<V> valueType) {
        return getCachingProvider().getCacheManager().getCache(cacheName, keyType, valueType);
    }


    // logic taken from RI API
    private static class CachingProviderRegistry {
        private WeakHashMap<ClassLoader, LinkedHashMap<String, CachingProvider>> cachingProviders;

        private volatile ClassLoader classLoader;

        public CachingProviderRegistry() {
            this.cachingProviders = new WeakHashMap<ClassLoader, LinkedHashMap<String, CachingProvider>>();
            this.classLoader = null;
        }

        public ClassLoader getDefaultClassLoader() {
            ClassLoader loader = classLoader;
            return loader == null ? Thread.currentThread().getContextClassLoader() : loader;
        }

        public void setDefaultClassLoader(ClassLoader classLoader) {
            this.classLoader = classLoader;
        }

        public CachingProvider getCachingProvider() {
            return getCachingProvider(getDefaultClassLoader());
        }

        public CachingProvider getCachingProvider(ClassLoader classLoader) {
            Iterator<CachingProvider> iterator = getCachingProviders(classLoader).iterator();
            if (iterator.hasNext()) {
                CachingProvider provider = iterator.next();
                if (iterator.hasNext()) {
                    throw new CacheException("Multiple CachingProviders have been configured when only a single CachingProvider is expected");
                } else {
                    return provider;
                }
            }
            throw new CacheException("No CachingProviders have been configured");
        }

        public Iterable<CachingProvider> getCachingProviders() {
            return getCachingProviders(getDefaultClassLoader());
        }

        public synchronized Iterable<CachingProvider> getCachingProviders(ClassLoader classLoader) {
            final ClassLoader serviceClassLoader = classLoader == null ? getDefaultClassLoader() : classLoader;
            LinkedHashMap<String, CachingProvider> providers = cachingProviders.get(serviceClassLoader);
            if (providers == null) {
                if (System.getProperties().containsKey(JAVAX_CACHE_CACHING_PROVIDER)) {
                    final String className = System.getProperty(JAVAX_CACHE_CACHING_PROVIDER);
                    providers = new LinkedHashMap<String, CachingProvider>();
                    providers.put(className, loadCachingProvider(className, serviceClassLoader));

                } else {
                    providers = AccessController.doPrivileged(new PrivilegedAction<LinkedHashMap<String, CachingProvider>>() {
                        public LinkedHashMap<String, CachingProvider> run() {
                            final LinkedHashMap<String, CachingProvider> result = new LinkedHashMap<String, CachingProvider>();
                            try {
                                final Class<?> clazz = Class.forName("org.apache.geronimo.osgi.locator.ProviderLocator");
                                final Method getServices = clazz.getDeclaredMethod("getServices", String.class, Class.class, ClassLoader.class);
                                for (final CachingProvider provider : (List<CachingProvider>) getServices.invoke(null, CachingProvider.class.getName(), CachingProvider.class, serviceClassLoader)) {
                                    result.put(provider.getClass().getName(), provider);
                                }
                            } catch (final Throwable e) {
                                // locator not available, try normal mode
                            }
                            final ServiceLoader<CachingProvider> serviceLoader = ServiceLoader.load(CachingProvider.class, serviceClassLoader);
                            for (final CachingProvider provider : serviceLoader) {
                                result.put(provider.getClass().getName(), provider);
                            }
                            return result;
                        }
                    });

                }
                cachingProviders.put(serviceClassLoader, providers);
            }
            return providers.values();
        }

        public CachingProvider getCachingProvider(String fullyQualifiedClassName) {
            return getCachingProvider(fullyQualifiedClassName, getDefaultClassLoader());
        }

        protected CachingProvider loadCachingProvider(String fullyQualifiedClassName, ClassLoader classLoader) throws CacheException {
            synchronized (classLoader) {
                try {
                    Class<?> clazz = classLoader.loadClass(fullyQualifiedClassName);
                    if (CachingProvider.class.isAssignableFrom(clazz)) {
                        return ((Class<CachingProvider>) clazz).newInstance();
                    } else {
                        throw new CacheException("The specified class [" + fullyQualifiedClassName + "] is not a CachingProvider");
                    }
                } catch (Exception e) {
                    throw new CacheException("Failed to load the CachingProvider [" + fullyQualifiedClassName + "]", e);
                }
            }
        }

        public synchronized CachingProvider getCachingProvider(String fullyQualifiedClassName, ClassLoader classLoader) {
            ClassLoader serviceClassLoader = classLoader == null ? getDefaultClassLoader() : classLoader;
            LinkedHashMap<String, CachingProvider> providers = cachingProviders.get(serviceClassLoader);
            if (providers == null) {
                getCachingProviders(serviceClassLoader);
                providers = cachingProviders.get(serviceClassLoader);
            }

            CachingProvider provider = providers.get(fullyQualifiedClassName);
            if (provider == null) {
                provider = loadCachingProvider(fullyQualifiedClassName, serviceClassLoader);
                providers.put(fullyQualifiedClassName, provider);
            }

            return provider;
        }
    }
}
