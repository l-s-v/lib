package com.lsv.lib.core.concept.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mappable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.behavior.Storable;

import java.io.Serializable;

public interface RepositoryImplementeable<
        I extends Identifiable<ID>,
        ID extends Serializable,
        P extends Persistable<ID>,
        S extends Storable<P, ID>>
        extends
        Repository<I> {

    default S storable() {
        return (S) Storable.<P, ID>findInstance(this, Storable.class);
    }

    default Mappable<I, P> mappable() {
        return (Mappable<I, P>) Mappable.findInstance(this, Identifiable.class, Persistable.class);
    }
}