package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.validations.Validable;
import lombok.*;

import java.util.List;

@RequiredArgsConstructor
@NoArgsConstructor
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
public class ServiceProviderImpl<
    I extends Identifiable<?>,
    R extends Repository<I>>
    implements
    ServiceProvider<I, R> {

    @NonNull
    private List<Validable<I>> validables;
    @NonNull
    private R repository;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public ServiceProviderImpl<I, R> configureRequiredWhenByService(Object sourceBase) {
        repository(findRepository(sourceBase));
        validables(validablesDefault(sourceBase));
        return this;
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <
        I extends Identifiable<?>,
        R extends Repository<I>>
    ServiceProviderImpl<I, R> of(Object sourceBase) {
        return new ServiceProviderImpl<I, R>().configureRequiredWhenByService(sourceBase);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressWarnings("unchecked")
    protected List<Validable<I>> validablesDefault(Object sourceBase) {
        return (List<Validable<I>>) (Object) VALIDABLES;
    }

    protected R findRepository(Object sourceBase) {
        return Repository.findInstance(sourceBase);
    }
}