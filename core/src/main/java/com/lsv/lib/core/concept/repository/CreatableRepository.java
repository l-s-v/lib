package com.lsv.lib.core.concept.repository;

import com.lsv.lib.core.behavior.Creatable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.behavior.Storable;
import lombok.NonNull;

import java.io.Serializable;

public interface CreatableRepository<
    I extends Identifiable<ID>,
    ID extends Serializable,
    P extends Persistable<ID>,
    S extends Storable<P, ID>>
    extends
    RepositoryImplementeable<I, ID, P, S>,
    Creatable<I> {

    @Override
    default I create(@NonNull I identifiable) {
        return repositoryProvider().mappable().of(
            repositoryProvider().storable().save(
                repositoryProvider().mappable().to(identifiable)));
    }
}