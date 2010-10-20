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

public class CompositeELResolver extends ELResolver {

    private int size;

    private ELResolver[] resolvers;

    public CompositeELResolver() {
        this.size = 0;
        this.resolvers = new ELResolver[2];
    }

    synchronized public void add(ELResolver elResolver) {
        if (elResolver == null) {
            throw new NullPointerException();
        }

        if (this.size >= this.resolvers.length) {
            ELResolver[] nr = new ELResolver[this.size * 2];
            System.arraycopy(this.resolvers, 0, nr, 0, this.size);
            this.resolvers = nr;
        }
        this.resolvers[this.size++] = elResolver;
    }

    public Object getValue(ELContext context, Object base, Object property)
            throws NullPointerException, PropertyNotFoundException, ELException {
        context.setPropertyResolved(false);
                int sz;
                ELResolver[] rslvrs;
                synchronized (this) {
                    sz = this.size;
                    rslvrs = this.resolvers;
                }
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
                int sz;
                ELResolver[] rslvrs;
                synchronized (this) {
                    sz = this.size;
                    rslvrs = this.resolvers;
                }
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
                int sz;
                ELResolver[] rslvrs;
                synchronized (this) {
                    sz = this.size;
                    rslvrs = this.resolvers;
                }
        boolean readOnly = false;
        for (int i = 0; i < sz; i++) {
            readOnly = rslvrs[i].isReadOnly(context, base, property);
            if (context.isPropertyResolved()) {
                return readOnly;
            }
        }
        return false;
    }

    synchronized public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        return new FeatureIterator(context, base, this.resolvers, this.size);
    }

    public Class<?> getCommonPropertyType(ELContext context, Object base) {
                int sz;
                ELResolver[] rslvrs;
                synchronized (this) {
                    sz = this.size;
                    rslvrs = this.resolvers;
                }
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
                int sz;
                ELResolver[] rslvrs;
                synchronized (this) {
                    sz = this.size;
                    rslvrs = this.resolvers;
                }
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
        ExpressionFactory expressionFactory = null;
        if (ELUtils.isCachedExpressionFactoryEnabled()) {
            expressionFactory = ELUtils.getCachedExpressionFactory();
        }
        if (expressionFactory == null) {
            expressionFactory = ExpressionFactory.newInstance();
        }
        String targetMethod = (String) expressionFactory.coerceToType(method, String.class);
        if (targetMethod.length() == 0) {
            throw new ELException(new NoSuchMethodException());
        }
        context.setPropertyResolved(false);
        if (context.getContext(ExpressionFactory.class) == null) {
            context.putContext(ExpressionFactory.class, expressionFactory);
        }
        Object retValue = null;
        for (ELResolver resolver : resolvers) {
            retValue = resolver.invoke(context, base, targetMethod, paramTypes, params);
            if (context.isPropertyResolved()) {
                return retValue;
            }
        }
        return null;
    }
}
