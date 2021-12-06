package com.lsv.lib.core.concept.repository;

import com.lsv.lib.core.behavior.Deletable;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.behavior.Storable;
import lombok.NonNull;

import java.io.Serializable;

public interface DeletableRepository<
    I extends Identifiable<ID>,
    ID extends Serializable,
    P extends Persistable<ID>,
    S extends Storable<P, ID>>
    extends
    RepositoryImplementeable<I, ID, P, S>,
    Deletable<I> {

    @Override
    default void delete(@NonNull I identifiable) {
        repositoryProvider().storable().deleteById(identifiable.getId());
    }
}