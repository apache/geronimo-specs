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

import java.io.Serializable;

public final class FactoryBuilder {
    private FactoryBuilder() {
        // no-op
    }

    public static <T> Factory<T> factoryOf(Class<T> clazz) {
        return new ClassFactory<T>(clazz);
    }

    public static <T> Factory<T> factoryOf(String className) {
        return new ClassFactory<T>(className);
    }

    public static <T extends Serializable> Factory<T> factoryOf(T instance) {
        return new SingletonFactory<T>(instance);
    }

    public static class ClassFactory<T> implements Factory<T>, Serializable {
        public static final long serialVersionUID = 201401L;

        private String className;

        public ClassFactory(Class<T> clazz) {
            this.className = clazz.getName();
        }

        public ClassFactory(String className) {
            this.className = className;
        }


        public T create() {
            try {
                final ClassLoader loader = Thread.currentThread().getContextClassLoader();
                return (T) loader.loadClass(className).newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Failed to create an instance of " + className, e);
            }
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) return true;
            if (other == null || getClass() != other.getClass()) return false;

            final ClassFactory that = ClassFactory.class.cast(other);
            return className.equals(that.className);

        }

        @Override
        public int hashCode() {
            return className.hashCode();
        }
    }

    public static class SingletonFactory<T> implements Factory<T>, Serializable {

        public static final long serialVersionUID = 201402;

        private T instance;

        public SingletonFactory(T instance) {
            this.instance = instance;
        }


        public T create() {
            return instance;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (other == null || getClass() != other.getClass()) return false;

            final SingletonFactory that = SingletonFactory.class.cast(other);

            return instance.equals(that.instance);

        }

        @Override
        public int hashCode() {
            return instance.hashCode();
        }
    }
}
