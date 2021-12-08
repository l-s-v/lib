package com.lsv.lib.core.behavior;

import com.lsv.lib.core.pattern.register.RegisterByInterface;

import java.io.Serializable;
import java.util.Optional;

public interface Storable<
    P extends Persistable<ID>,
    ID extends Serializable> {

    <S extends P> S save(S entity);

    <S extends P> Iterable<S> saveAll(Iterable<S> entities);

    Optional<P> findById(ID id);

    boolean existsById(ID id);

    Iterable<P> findAll();

    Iterable<P> findAllById(Iterable<ID> ids);

    long count();

    void deleteById(ID id);

    void delete(P entity);

    void deleteAllById(Iterable<? extends ID> ids);

    void deleteAll(Iterable<? extends P> entities);

    void deleteAll();

    @SuppressWarnings("unchecked")
    static <P extends Persistable<ID>, ID extends Serializable> Storable<P, ID> findInstance() {
        return RegisterByInterface.findImplementation(Storable.class);
    }
}