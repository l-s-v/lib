package com.lsv.lib.core.concept.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.behavior.Storable;
import com.lsv.lib.core.concept.dto.Filter;
import com.lsv.lib.core.concept.dto.ListDto;
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
        return repositoryProvider().storable().findById(identifiable.getId())
            .map(p -> repositoryProvider().mappable().of(p));
    }

    @Override
    default ListDto<I> findByFilter(@NonNull Filter<I> filter) {
        throw new IllegalCallerException("Basic implementation does not contain this method. " +
            "A new implementation of ReadableRepository must be provided.");
    }
}