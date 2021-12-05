package com.lsv.lib.core.concept.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.behavior.Storable;
import lombok.NonNull;

import java.io.Serializable;
import java.util.Optional;

public interface ReadableRepository<
    I extends Identifiable<ID>,
    ID extends Serializable,
    P extends Persistable<ID>,
    S extends Storable<P, ID>>
    extends
    RepositoryImplementeable<I, ID, P, S>,
    Readable<I> {

    @Override
    default Optional<I> findById(@NonNull I identifiable) {
        return providerRepository().storable().findById(identifiable.getId())
            .map(p -> providerRepository().mappable().of(p));
    }
}