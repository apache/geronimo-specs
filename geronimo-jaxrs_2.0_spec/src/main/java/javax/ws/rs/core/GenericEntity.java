/*
 * #%L
 * Apache Geronimo JAX-RS Spec 2.0
 * %%
 * Copyright (C) 2003 - 2014 The Apache Software Foundation
 * %%
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
 * #L%
 */

package javax.ws.rs.core;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


public class GenericEntity<T> {

    private final Class<?> rawType;
    private final Type type;
    private final T entity;


    protected GenericEntity(final T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("The entity must not be null");
        }
        this.entity = entity;
        this.type = GenericType.getTypeArgument(getClass(), GenericEntity.class);
        this.rawType = entity.getClass();
    }


    public GenericEntity(final T entity, final Type genericType) {
        if (entity == null || genericType == null) {
            throw new IllegalArgumentException("Arguments must not be null.");
        }
        this.entity = entity;
        this.rawType = entity.getClass();
        checkTypeCompatibility(this.rawType, genericType);
        this.type = genericType;
    }

    private void checkTypeCompatibility(final Class<?> c, final Type t) {
        if (t instanceof Class) {
            Class<?> ct = (Class<?>) t;
            if (ct.isAssignableFrom(c)) {
                return;
            }
        } else if (t instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) t;
            Type rt = pt.getRawType();
            checkTypeCompatibility(c, rt);
            return;
        } else if (c.isArray() && (t instanceof GenericArrayType)) {
            GenericArrayType at = (GenericArrayType) t;
            Type rt = at.getGenericComponentType();
            checkTypeCompatibility(c.getComponentType(), rt);
            return;
        }
        throw new IllegalArgumentException("The type is incompatible with the class of the entity.");
    }


    public final Class<?> getRawType() {
        return rawType;
    }


    public final Type getType() {
        return type;
    }


    public final T getEntity() {
        return entity;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = this == obj;
        if (!result && obj instanceof GenericEntity) {

            GenericEntity<?> that = (GenericEntity<?>) obj;
            return this.type.equals(that.type) && this.entity.equals(that.entity);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return entity.hashCode() + type.hashCode() * 37 + 5;
    }

    @Override
    public String toString() {
        return "GenericEntity{" + entity.toString() + ", " + type.toString() + "}";
    }
}
