package com.lsv.lib.core.concept.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mappable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.behavior.Storable;
import lombok.*;

import java.io.Serializable;

@RequiredArgsConstructor
@NoArgsConstructor
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
public class RepositoryProviderImpl<
    I extends Identifiable<ID>,
    ID extends Serializable,
    P extends Persistable<ID>,
    S>
    implements
    RepositoryProvider<I, ID, P, S> {

    @NonNull
    private Mappable<I, P> mappable;
    @NonNull
    private S storable;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressWarnings("unchecked")
    @Override
    public RepositoryProvider<I, ID, P, S> configureRequiredWhenByService(Object sourceBase) {
        mappable((Mappable<I, P>) findMapper(sourceBase));
        storable(findStorable(sourceBase));
        return this;
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <
        I extends Identifiable<ID>,
        ID extends Serializable,
        P extends Persistable<ID>,
        S>
    RepositoryProvider<I, ID, P, S> of(Object sourceBase) {
        return new RepositoryProviderImpl<I, ID, P, S>().configureRequiredWhenByService(sourceBase);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    protected Mappable<?, ?> findMapper(Object sourceBase) {
        return Mappable.findInstance(sourceBase);
    }

    @SuppressWarnings("unchecked")
    protected S findStorable(Object sourceBase) {
        return (S) Storable.<P, ID>findInstance();
    }
}