/*
 **
 ** Licensed to the Apache Software Foundation (ASF) under one
 ** or more contributor license agreements.  See the NOTICE file
 ** distributed with this work for additional information
 ** regarding copyright ownership.  The ASF licenses this file
 ** to you under the Apache License, Version 2.0 (the
 ** "License"); you may not use this file except in compliance
 ** with the License.  You may obtain a copy of the License at
 **
 **  http://www.apache.org/licenses/LICENSE-2.0
 **
 ** Unless required by applicable law or agreed to in writing,
 ** software distributed under the License is distributed on an
 ** "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 ** KIND, either express or implied.  See the License for the
 ** specific language governing permissions and limitations
 ** under the License.
 */
package javax.xml.bind;

import java.io.Serializable;

import javax.xml.namespace.QName;

public class JAXBElement<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public final static class GlobalScope {
    }

    protected final QName name;
    protected final Class<T> declaredType;
    protected final Class scope;
    protected T value;
    protected boolean nil;

    public JAXBElement(QName name, Class<T> declaredType, Class scope, T value) {
        this.nil = false;
        if (declaredType == null || name == null) {
            throw new IllegalArgumentException();
        }
        this.declaredType = declaredType;
        if (scope == null) {
            scope = GlobalScope.class;
        }
        this.scope = scope;
        this.name = name;
        setValue(value);
    }

    public JAXBElement(QName name, Class<T> declaredType, T value) {
        this(name, declaredType, GlobalScope.class, value);
    }

    public Class<T> getDeclaredType() {
        return declaredType;
    }

    public QName getName() {
        return name;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public Class getScope() {
        return scope;
    }

    public boolean isNil() {
        return value == null || nil;
    }

    public void setNil(boolean nil) {
        this.nil = nil;
    }

    public boolean isGlobalScope() {
        return scope == GlobalScope.class;
    }

    public boolean isTypeSubstituted() {
        if (value == null) {
            return false;
        } else {
            return value.getClass() != declaredType;
        }
    }
}
