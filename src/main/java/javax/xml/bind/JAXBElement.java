package javax.xml.bind;

import java.io.Serializable;

import javax.xml.namespace.QName;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 9:35:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class JAXBElement<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static class GlobalScope {
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
