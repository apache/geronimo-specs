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
package javax.cache.annotation;


import javax.enterprise.util.Nonbinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheResult {
    @Nonbinding String cacheName() default "";

    @Nonbinding boolean skipGet() default false;

    @Nonbinding Class<? extends CacheResolverFactory> cacheResolverFactory() default CacheResolverFactory.class;

    @Nonbinding Class<? extends CacheKeyGenerator> cacheKeyGenerator() default CacheKeyGenerator.class;

    @Nonbinding String exceptionCacheName() default "";

    @Nonbinding Class<? extends Throwable>[] cachedExceptions() default {};

    @Nonbinding Class<? extends Throwable>[] nonCachedExceptions() default {};
}
