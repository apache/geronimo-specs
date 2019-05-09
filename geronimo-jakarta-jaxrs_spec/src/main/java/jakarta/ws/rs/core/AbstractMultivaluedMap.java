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
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package jakarta.ws.rs.core;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractMultivaluedMap<K, V> implements MultivaluedMap<K, V> {

    protected final Map<K, List<V>> store;

    public AbstractMultivaluedMap(Map<K, List<V>> store) {
        if (store == null) {
            throw new NullPointerException("Underlying store must not be 'null'.");
        }
        this.store = store;
    }

    @Override
    public final void putSingle(K key, V value) {
        List<V> values = getValues(key);

        values.clear();
        if (value != null) {
            values.add(value);
        } else {
            addNull(values);
        }
    }

    @SuppressWarnings("UnusedParameters")
    protected void addNull(List<V> values) {

    }

    @SuppressWarnings("UnusedParameters")
    protected void addFirstNull(List<V> values) {

    }

    @Override
    public final void add(K key, V value) {
        List<V> values = getValues(key);

        if (value != null) {
            values.add(value);
        } else {
            addNull(values);
        }
    }

    @Override
    public final void addAll(K key, V... newValues) {
        if (newValues == null) {
            throw new NullPointerException("Supplied array of values must not be null.");
        }
        if (newValues.length == 0) {
            return;
        }

        List<V> values = getValues(key);

        for (V value : newValues) {
            if (value != null) {
                values.add(value);
            } else {
                addNull(values);
            }
        }
    }

    @Override
    public final void addAll(K key, List<V> valueList) {
        if (valueList == null) {
            throw new NullPointerException("Supplied list of values must not be null.");
        }
        if (valueList.isEmpty()) {
            return;
        }

        List<V> values = getValues(key);

        for (V value : valueList) {
            if (value != null) {
                values.add(value);
            } else {
                addNull(values);
            }
        }
    }

    @Override
    public final V getFirst(K key) {
        List<V> values = store.get(key);
        if (values != null && values.size() > 0) {
            return values.get(0);
        } else {
            return null;
        }
    }

    @Override
    public final void addFirst(K key, V value) {
        List<V> values = getValues(key);

        if (value != null) {
            values.add(0, value);
        } else {
            addFirstNull(values);
        }
    }

    protected final List<V> getValues(K key) {
        List<V> l = store.get(key);
        if (l == null) {
            l = new LinkedList<V>();
            store.put(key, l);
        }
        return l;
    }

    @Override
    public String toString() {
        return store.toString();
    }

    @Override
    public int hashCode() {
        return store.hashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        return store.equals(o);
    }

    @Override
    public Collection<List<V>> values() {
        return store.values();
    }

    @Override
    public int size() {
        return store.size();
    }

    @Override
    public List<V> remove(Object key) {
        return store.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends List<V>> m) {
        store.putAll(m);
    }

    @Override
    public List<V> put(K key, List<V> value) {
        return store.put(key, value);
    }

    @Override
    public Set<K> keySet() {
        return store.keySet();
    }

    @Override
    public boolean isEmpty() {
        return store.isEmpty();
    }

    @Override
    public List<V> get(Object key) {
        return store.get(key);
    }

    @Override
    public Set<Entry<K, List<V>>> entrySet() {
        return store.entrySet();
    }

    @Override
    public boolean containsValue(Object value) {
        return store.containsValue(value);
    }

    @Override
    public boolean containsKey(Object key) {
        return store.containsKey(key);
    }

    @Override
    public void clear() {
        store.clear();
    }

    @Override
    public boolean equalsIgnoreValueOrder(MultivaluedMap<K, V> omap) {
        if (this == omap) {
            return true;
        }
        if (!keySet().equals(omap.keySet())) {
            return false;
        }
        for (Entry<K, List<V>> e : entrySet()) {
            List<V> olist = omap.get(e.getKey());
            if (e.getValue().size() != olist.size()) {
                return false;
            }
            for (V v : e.getValue()) {
                if (!olist.contains(v)) {
                    return false;
                }
            }
        }
        return true;
    }
}
