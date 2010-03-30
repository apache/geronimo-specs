/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */

package javax.ws.rs.core;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericEntity<T> {
    private final T    entity;
    private final Type genericType;

    protected GenericEntity(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException();
        }
        this.entity = entity;

        Type superclass = this.getClass().getGenericSuperclass();
        this.genericType = ((ParameterizedType)superclass).getActualTypeArguments()[0];

        // TODO: what if the user extends their GenericEntity superclass?
    }

    public GenericEntity(T entity, Type genericType) {
        if (entity == null) {
            throw new IllegalArgumentException();
        }

        if (genericType == null) {
            throw new IllegalArgumentException();
        }

        checkCompatibility(entity.getClass(), genericType);
        this.entity = entity;
        this.genericType = genericType;
    }

    private void checkCompatibility(Class<?> rawType, Type genericType) {
        if (genericType instanceof Class<?>) {
            if (!((Class<?>)genericType).isAssignableFrom(rawType)) {
                throw new IllegalArgumentException();
            }
        } else if (genericType instanceof ParameterizedType) {
            checkCompatibility(rawType, ((ParameterizedType)genericType).getRawType());
        } else if (genericType instanceof GenericArrayType) {
            if (!rawType.isArray()) {
                throw new IllegalArgumentException();
            }
            // check the array parameter
            checkCompatibility(rawType.getComponentType(), ((GenericArrayType)genericType)
                .getGenericComponentType());
        }
    }

    public T getEntity() {
        return entity;
    }

    public Class<?> getRawType() {
        return entity.getClass();
    }

    public Type getType() {
        return genericType;
    }
}
