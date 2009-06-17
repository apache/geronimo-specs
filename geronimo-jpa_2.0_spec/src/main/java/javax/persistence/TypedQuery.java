package javax.persistence;

import java.util.List;

/**
 * Interface used to control the execution of typed queries.
 */
public interface TypedQuery<X> extends Query {
    
    List<X> getResultList();

    X getSingleResult();

    TypedQuery<X> setMaxResults(int maxResult);

    TypedQuery<X> setFirstResult(int startPosition);

    TypedQuery<X> setHint(String hintName, Object value);

    <T> TypedQuery<X> setParameter(Parameter<T> param, T value);

    TypedQuery<X> setFlushMode(FlushModeType flushMode);

    TypedQuery<X> setLockMode(LockModeType lockMode);
}
