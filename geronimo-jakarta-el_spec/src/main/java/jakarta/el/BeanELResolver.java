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

import java.beans.BeanInfo;
import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;


public class BeanELResolver extends ELResolver {

    private final boolean readOnly;

    private final ConcurrentCache<String, BeanProperties> cache = new ConcurrentCache<String, BeanProperties>(
            1000);

    public BeanELResolver() {
        this.readOnly = false;
    }

    public BeanELResolver(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Object getValue(ELContext context, Object base, Object property)
            throws NullPointerException, PropertyNotFoundException, ELException {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base == null || property == null) {
            return null;
        }

        context.setPropertyResolved(true);
        Method m = this.property(context, base, property).read(context);
        try {
            return m.invoke(base, (Object[]) null);
        } catch (IllegalAccessException e) {
            throw new ELException(e);
        } catch (InvocationTargetException e) {
            throw new ELException(message(context, "propertyReadError",
                    new Object[] { base.getClass().getName(),
                            property.toString() }), e.getCause());
        } catch (Exception e) {
            throw new ELException(e);
        }
    }

    public Class<?> getType(ELContext context, Object base, Object property)
            throws NullPointerException, PropertyNotFoundException, ELException {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base == null || property == null) {
            return null;
        }

        context.setPropertyResolved(true);
        return this.property(context, base, property).getPropertyType();
    }

    public void setValue(ELContext context, Object base, Object property,
            Object value) throws NullPointerException,
            PropertyNotFoundException, PropertyNotWritableException,
            ELException {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base == null || property == null) {
            return;
        }

        context.setPropertyResolved(true);

        if (this.readOnly) {
            throw new PropertyNotWritableException(message(context,
                    "resolverNotWriteable", new Object[] { base.getClass()
                            .getName() }));
        }

        Method m = this.property(context, base, property).write(context);
        try {
            m.invoke(base, value);
        } catch (IllegalAccessException e) {
            throw new ELException(e);
        } catch (InvocationTargetException e) {
            throw new ELException(message(context, "propertyWriteError",
                    new Object[] { base.getClass().getName(),
                            property.toString() }), e.getCause());
        } catch (Exception e) {
            throw new ELException(e);
        }
    }

    public boolean isReadOnly(ELContext context, Object base, Object property)
            throws NullPointerException, PropertyNotFoundException, ELException {
        if (context == null) {
            throw new NullPointerException();
        }
        if (base == null || property == null) {
            return false;
        }

        context.setPropertyResolved(true);
        return this.readOnly
                || this.property(context, base, property).isReadOnly();
    }

    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        if (context == null) {
            throw new NullPointerException();
        }

        if (base == null) {
            return null;
        }

        try {
            BeanInfo info = Introspector.getBeanInfo(base.getClass());
            PropertyDescriptor[] pds = info.getPropertyDescriptors();
            for (PropertyDescriptor pd : pds) {
                pd.setValue(RESOLVABLE_AT_DESIGN_TIME, Boolean.TRUE);
                pd.setValue(TYPE, pd.getPropertyType());
            }
            return Arrays.asList((FeatureDescriptor[]) pds).iterator();
        } catch (IntrospectionException e) {
            //
        }

        return null;
    }

    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        if (context == null) {
            throw new NullPointerException();
        }

        if (base != null) {
            return Object.class;
        }

        return null;
    }

    protected final static class BeanProperties {
        private final Map<String, BeanProperty> properties;

        private final Class<?> type;

        public BeanProperties(Class<?> type) throws ELException {
            this.type = type;
            this.properties = new HashMap<String, BeanProperty>();
            try {
                BeanInfo info = Introspector.getBeanInfo(this.type);
                PropertyDescriptor[] pds = info.getPropertyDescriptors();
                for (PropertyDescriptor pd : pds) {
                    this.properties.put(pd.getName(), new BeanProperty(
                            type, pd));
                }
            } catch (IntrospectionException ie) {
                throw new ELException(ie);
            }
        }

        private BeanProperty get(ELContext ctx, String name) {
            BeanProperty property = this.properties.get(name);
            if (property == null) {
                throw new PropertyNotFoundException(message(ctx,
                        "propertyNotFound",
                        new Object[] { type.getName(), name }));
            }
            return property;
        }

        public BeanProperty getBeanProperty(String name) {
            return get(null, name);
        }

        private Class<?> getType() {
            return type;
        }
    }

    protected final static class BeanProperty {
        private final Class<?> type;

        private final Class<?> owner;

        private final PropertyDescriptor descriptor;

        private Method read;

        private Method write;

        public BeanProperty(Class<?> owner, PropertyDescriptor descriptor) {
            this.owner = owner;
            this.descriptor = descriptor;
            this.type = descriptor.getPropertyType();
        }

        public Class getPropertyType() {
            return this.type;
        }

        public boolean isReadOnly() {
            return this.write == null
                && (null == (this.write = getMethod(this.owner, descriptor.getWriteMethod())));
        }

        public Method getWriteMethod() {
            return write(null);
        }

        public Method getReadMethod() {
            return this.read(null);
        }

        private Method write(ELContext ctx) {
            if (this.write == null) {
                this.write = getMethod(this.owner, descriptor.getWriteMethod());
                if (this.write == null) {
                    throw new PropertyNotFoundException(message(ctx,
                            "propertyNotWritable", new Object[] {
                                    type.getName(), descriptor.getName() }));
                }
            }
            return this.write;
        }

        private Method read(ELContext ctx) {
            if (this.read == null) {
                this.read = getMethod(this.owner, descriptor.getReadMethod());
                if (this.read == null) {
                    throw new PropertyNotFoundException(message(ctx,
                            "propertyNotReadable", new Object[] {
                                    type.getName(), descriptor.getName() }));
                }
            }
            return this.read;
        }
    }

    private BeanProperty property(ELContext ctx, Object base,
            Object property) {
        Class<?> type = base.getClass();
        String prop = property.toString();

        BeanProperties props = this.cache.get(type.getName());
        if (props == null || type != props.getType()) {
            props = new BeanProperties(type);
            this.cache.put(type.getName(), props);
        }

        return props.get(ctx, prop);
    }

    private static Method getMethod(Class type, Method m) {
        if (m == null || Modifier.isPublic(type.getModifiers())) {
            return m;
        }
        Class[] inf = type.getInterfaces();
        Method mp;
        for (Class anInf : inf) {
            try {
                mp = anInf.getMethod(m.getName(), m.getParameterTypes());
                mp = getMethod(mp.getDeclaringClass(), mp);
                if (mp != null) {
                    return mp;
                }
            } catch (NoSuchMethodException e) {
                //continue
            }
        }
        Class sup = type.getSuperclass();
        if (sup != null) {
            try {
                mp = sup.getMethod(m.getName(), m.getParameterTypes());
                mp = getMethod(mp.getDeclaringClass(), mp);
                if (mp != null) {
                    return mp;
                }
            } catch (NoSuchMethodException e) {
                //continue
            }
        }
        return null;
    }

    private final static class ConcurrentCache<K,V> {

        private final int size;
        private final Map<K,V> eden;
        private final Map<K,V> longterm;

        public ConcurrentCache(int size) {
            this.size = size;
            this.eden = new ConcurrentHashMap<K,V>(size);
            this.longterm = new WeakHashMap<K,V>(size);
        }

        public V get(K key) {
            V value = this.eden.get(key);
            if (value == null) {
                value = this.longterm.get(key);
                if (value != null) {
                    this.eden.put(key, value);
                }
            }
            return value;
        }

        public void put(K key, V value) {
            if (this.eden.size() >= this.size) {
                this.longterm.putAll(this.eden);
                this.eden.clear();
            }
            this.eden.put(key, value);
        }

    }

    public Object invoke(ELContext context, Object base, Object method, Class<?>[] paramTypes, Object[] params) {
        if (context == null) {
            throw new NullPointerException("ELContext could not be nulll");
        }
        // Why static invocation is not supported
        if (base == null || method == null) {
            return null;
        }

        String methodName = ELUtils.coerceToString(method);
        if (methodName.length() == 0) {
            throw new MethodNotFoundException("The parameter method could not be zero-length");
        }
        Class<?> targetClass = base.getClass();
        if (methodName.equals("<init>") || methodName.equals("<cinit>")) {
            throw new MethodNotFoundException(method + " is not found in target class " + targetClass.getName());
        }

        if (params == null) {
            params = new Object[0];
        }

        Method targetMethod = null;
        if (paramTypes == null) {
            int paramsNumber = params.length;
            Method[] methods = targetClass.getMethods();
            for (Method m : methods) {
                if (m.getName().equals(methodName) && m.getParameterTypes().length == paramsNumber) {
                    targetMethod = m;
                    break;
                }
            }
            if (targetMethod == null) {
                for (Method m : methods) {
                    if (m.getName().equals(methodName) && m.isVarArgs() && paramsNumber >= (m.getParameterTypes().length - 1)) {
                        targetMethod = m;
                        break;
                    }
                }
            }
        } else {
            try {
                targetMethod = targetClass.getMethod(methodName, paramTypes);
            } catch (SecurityException e) {
                throw new ELException(e);
            } catch (NoSuchMethodException e) {
                throw new MethodNotFoundException(e);
            }
        }
        if (targetMethod == null) {
            throw new MethodNotFoundException(method + " is not found in target class " + targetClass.getName());
        }
        if (paramTypes == null) {
            paramTypes = targetMethod.getParameterTypes();
        }
        //Initial check whether the types and parameter values length
        if (targetMethod.isVarArgs()) {
            if (paramTypes.length - 1 > params.length) {
                throw new IllegalArgumentException("Inconsistent number between argument types and values");
            }
        } else if (paramTypes.length != params.length) {
            throw new IllegalArgumentException("Inconsistent number between argument types and values");
        }
        try {
            Object[] finalParamValues = new Object[paramTypes.length];
            //Only do the parameter conversion while the method is not a non-parameter one
            if (paramTypes.length > 0) {
                ExpressionFactory expressionFactory = null;
                if (ELUtils.isCachedExpressionFactoryEnabled()) {
                    expressionFactory = ELUtils.getCachedExpressionFactory();
                }
                if (expressionFactory == null) {
                    expressionFactory = ExpressionFactory.newInstance();
                }

                int iCurrentIndex = 0;
                for (int iLoopSize = paramTypes.length - 1; iCurrentIndex < iLoopSize; iCurrentIndex++) {
                    finalParamValues[iCurrentIndex] = expressionFactory.coerceToType(params[iCurrentIndex], paramTypes[iCurrentIndex]);
                }
                /**
                 * Not sure it is over-designed. Do not find detailed description about how the parameter values are passed if the method is of variable arguments.
                 * It might be an array directly passed or each parameter value passed one by one.
                 */
                if (targetMethod.isVarArgs()) {
                    // varArgsClassType should be an array type
                    Class<?> varArgsClassType = paramTypes[iCurrentIndex];
                    // 1. If there is no parameter value left for the variable argument, create a zero-length array
                    // 2. If there is only one parameter value left for the variable argument, and it has the same array type with the varArgsClass, pass in directly
                    // 3. Else, create an array of varArgsClass type, and add all the left coerced parameter values
                    if (iCurrentIndex == params.length) {
                        finalParamValues[iCurrentIndex] = Array.newInstance(varArgsClassType.getComponentType(), 0);
                    } else if (iCurrentIndex == params.length - 1 && varArgsClassType == params[iCurrentIndex].getClass()
                            && varArgsClassType.getClassLoader() == params[iCurrentIndex].getClass().getClassLoader()) {
                        finalParamValues[iCurrentIndex] = params[iCurrentIndex];
                    } else {
                        Object targetArray = Array.newInstance(varArgsClassType.getComponentType(), params.length - iCurrentIndex);
                        Class<?> componentClassType = varArgsClassType.getComponentType();
                        for (int i = 0, iLoopSize = params.length - iCurrentIndex; i < iLoopSize; i++) {
                            Array.set(targetArray, i, expressionFactory.coerceToType(params[iCurrentIndex + i], componentClassType));
                        }
                        finalParamValues[iCurrentIndex] = targetArray;
                    }
                } else {
                    finalParamValues[iCurrentIndex] = expressionFactory.coerceToType(params[iCurrentIndex], paramTypes[iCurrentIndex]);
                }
            }
            Object retValue = targetMethod.invoke(base, finalParamValues);
            context.setPropertyResolved(true);
            return retValue;
        }  catch (IllegalAccessException e) {
            throw new ELException(e);
        } catch (InvocationTargetException e) {
            throw new ELException(e.getCause());
        }
    }
}
