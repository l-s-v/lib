package com.lsv.lib.core.test.concept.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.mapper.HelperMappable;
import com.lsv.lib.core.mapper.Mappable;
import com.lsv.lib.core.test.TestForFactory;

public interface TestRepository<
        D extends Identifiable<?>,
        R>
        extends
        TestRepositoryProvider<D>,
        TestForFactory {

    R repository();

    default Mappable<Identifiable<?>, Persistable<?>> mappable() {
        return (Mappable<Identifiable<?>, Persistable<?>>) HelperMappable.of(this, Identifiable.class, Persistable.class);
    }
}