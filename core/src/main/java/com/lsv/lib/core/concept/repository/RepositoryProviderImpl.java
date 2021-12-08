package com.lsv.lib.core.concept.repository;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mappable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.behavior.Storable;
import com.lsv.lib.core.pattern.register.RegisterByInterface;
import lombok.*;

import java.io.Serializable;
import java.util.Optional;

@RequiredArgsConstructor
@NoArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Getter
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
    @Setter
    @Getter(AccessLevel.PROTECTED)
    private Object setupRequiredWhenByService;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressWarnings("unchecked")
    @Override
    public Mappable<I, P> mappable() {
        return Optional.of(mappable).orElseGet(() ->
            mappable((Mappable<I, P>) findMapper())
                .mappable());
    }

    @Override
    public S storable() {
        return Optional.of(storable).orElseGet(() ->
            storable(findStorable())
                .storable());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressWarnings("unchecked")
    public static <
        I extends Identifiable<ID>,
        ID extends Serializable,
        P extends Persistable<ID>,
        S>
    RepositoryProviderImpl<I, ID, P, S> of(Object sourceBased) {
        return new RepositoryProviderImpl<>(
            (Mappable<I, P>) Mappable.findInstance(sourceBased),
            (S) Storable.<P, ID>findInstance()
        );
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    protected Mappable<?, ?> findMapper() {
        return Mappable.findInstance(setupRequiredWhenByService());
    }

    @SuppressWarnings("unchecked")
    protected S findStorable() {
        return (S) RegisterByInterface.findImplementation(Storable.class);
    }
}