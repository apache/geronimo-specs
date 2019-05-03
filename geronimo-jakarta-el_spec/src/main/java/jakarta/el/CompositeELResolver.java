/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package javax.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

public class CompositeELResolver extends ELResolver {

    private AtomicReference<ELResolver[]> resolvers;

    public CompositeELResolver() {
        this.resolvers = new AtomicReference<ELResolver[]>(new ELResolver[0]);
    }

    synchronized public void add(ELResolver elResolver) {
        if (elResolver == null) {
            throw new NullPointerException();
        }

        ELResolver[] rslvrs = resolvers.get();
        int sz = rslvrs.length;
        
        ELResolver[] nr = new ELResolver[sz + 1];
        System.arraycopy(rslvrs, 0, nr, 0, sz);
        nr[sz] = elResolver;
        
        resolvers.set(nr);
    }

    public Object getValue(ELContext context, Object base, Object property)
            throws NullPointerException, PropertyNotFoundException, ELException {
        context.setPropertyResolved(false);

        ELResolver[] rslvrs = resolvers.get();
        int sz = rslvrs.length;

        Object result = null;
        for (int i = 0; i < sz; i++) {
            result = rslvrs[i].getValue(context, base, property);
            if (context.isPropertyResolved()) {
                return result;
            }
        }
        return null;
    }

    public void setValue(ELContext context, Object base, Object property,
            Object value) throws NullPointerException,
            PropertyNotFoundException, PropertyNotWritableException,
            ELException {
        context.setPropertyResolved(false);

        ELResolver[] rslvrs = resolvers.get();
        int sz = rslvrs.length;

        for (int i = 0; i < sz; i++) {
            rslvrs[i].setValue(context, base, property, value);
            if (context.isPropertyResolved()) {
                return;
            }
        }
    }

    public boolean isReadOnly(ELContext context, Object base, Object property)
            throws NullPointerException, PropertyNotFoundException, ELException {
        context.setPropertyResolved(false);

        ELResolver[] rslvrs = resolvers.get();
        int sz = rslvrs.length;

        boolean readOnly = false;
        for (int i = 0; i < sz; i++) {
            readOnly = rslvrs[i].isReadOnly(context, base, property);
            if (context.isPropertyResolved()) {
                return readOnly;
            }
        }
        return false;
    }

    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {        
        ELResolver[] rslvrs = resolvers.get();
        int sz = rslvrs.length;
        return new FeatureIterator(context, base, rslvrs, sz);
    }

    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        ELResolver[] rslvrs = resolvers.get();
        int sz = rslvrs.length;

        Class<?> commonType = null, type = null;
        for (int i = 0; i < sz; i++) {
            type = rslvrs[i].getCommonPropertyType(context, base);
            if (type != null
                    && (commonType == null || commonType.isAssignableFrom(type))) {
                commonType = type;
            }
        }
        return commonType;
    }

    public Class<?> getType(ELContext context, Object base, Object property)
            throws NullPointerException, PropertyNotFoundException, ELException {
        context.setPropertyResolved(false);

        ELResolver[] rslvrs = resolvers.get();
        int sz = rslvrs.length;

        Class<?> type;
        for (int i = 0; i < sz; i++) {
            type = rslvrs[i].getType(context, base, property);
            if (context.isPropertyResolved()) {
                return type;
            }
        }
        return null;
    }

    private final static class FeatureIterator implements Iterator<FeatureDescriptor> {

        private final ELContext context;

        private final Object base;

        private final ELResolver[] resolvers;

        private final int size;

        private Iterator itr;

        private int idx;

        public FeatureIterator(ELContext context, Object base,
                ELResolver[] resolvers, int size) {
            this.context = context;
            this.base = base;
            this.resolvers = resolvers;
            this.size = size;

            this.idx = 0;
            this.guaranteeIterator();
        }

        private void guaranteeIterator() {
            while (this.itr == null && this.idx < this.size) {
                this.itr = this.resolvers[this.idx].getFeatureDescriptors(
                        this.context, this.base);
                this.idx++;
            }
        }

        public boolean hasNext() {
            return this.itr != null;
        }

        public FeatureDescriptor next() {
            Object result = null;
            if (this.itr != null) {
                if (this.itr.hasNext()) {
                    result = this.itr.next();
                    if (!this.itr.hasNext()) {
                        this.itr = null;
                        this.guaranteeIterator();
                    }
                }
            }
            return (FeatureDescriptor) result;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public Object invoke(ELContext context, Object base, Object method, Class<?>[] paramTypes, Object[] params) {
        if (context == null) {
            throw new NullPointerException("ELContext could not be null");
        }
        if (method == null || base == null) {
            return null;
        }
        String targetMethod = ELUtils.coerceToString(method);
        if (targetMethod.length() == 0) {
            throw new ELException(new NoSuchMethodException());
        }

        context.setPropertyResolved(false);

        ELResolver[] rslvrs = resolvers.get();
        int sz = rslvrs.length;

        Object retValue = null;
        for (int i = 0; i < sz; i++) {
            retValue = rslvrs[i].invoke(context, base, targetMethod, paramTypes, params);
            if (context.isPropertyResolved()) {
                return retValue;
            }
        }
        return null;
    }

}
