package com.lsv.lib.core.test.concept.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mappable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.test.TestForFactory;

public interface TestRepository<
        D extends Identifiable<?>,
        R extends Repository<D>>
        extends
        TestRepositoryProvider<D>,
        TestForFactory {

    @SuppressWarnings({"unchecked", "rawtypes"})
    default R repository() {
        return (R) Repository.findInstance(this, Repository.class);
    }

    default Mappable<Identifiable<?>, Persistable<?>> mappable() {
        return (Mappable<Identifiable<?>, Persistable<?>>) Mappable.findInstance(this, Identifiable.class, Persistable.class);
    }
}