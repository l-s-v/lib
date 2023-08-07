package com.lsv.lib.core.behavior;

import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.core.loader.Loader;

import java.io.Serializable;
import java.util.Optional;

/**
 * Defines the pattern of classes responsible for storing objects that can be persisted.
 *
 * @author Leandro da Silva Vieira
 */
public interface Storable<
        P extends Persistable<ID>,
        ID extends Serializable> {

    <S extends P> S save(S entity);

    <S extends P> S merge(S entity);

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

    static <P extends Persistable<ID>, ID extends Serializable> Storable<P, ID> findInstance(Object sourceBase, Class<?> sourceExtends) {
        return (Storable<P, ID>) Loader
                .of(HelperClass.identifyGenericsClass(sourceBase, sourceExtends))
                .findUniqueImplementationByFirstLoader();
    }

    /**
     * If, when saving the object, you must use the save method.
     * If it is false, then you must use merge.
     */
    default boolean isUtilizeSave() {
        return true;
    }
}