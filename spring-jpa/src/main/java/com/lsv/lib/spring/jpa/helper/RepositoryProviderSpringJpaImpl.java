package com.lsv.lib.spring.jpa.helper;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mappable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.concept.repository.RepositoryProviderImpl;
import com.lsv.lib.spring.jpa.repository.StorableSpringJpa;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

@NoArgsConstructor
public class RepositoryProviderSpringJpaImpl<
    I extends Identifiable<ID>,
    ID extends Serializable,
    P extends Persistable<ID>,
    S>
    extends
    RepositoryProviderImpl<I, ID, P, S> {

    public RepositoryProviderSpringJpaImpl(@NonNull Mappable<I, P> mappable, @NonNull S storable) {
        super(mappable, storable);
    }

    @SuppressWarnings("unchecked")
    public static <
        I extends Identifiable<ID>,
        ID extends Serializable,
        P extends Persistable<ID>,
        S>
    RepositoryProviderSpringJpaImpl<I, ID, P, S> of(Object sourceBased) {
        return new RepositoryProviderSpringJpaImpl<>(
            (Mappable<I, P>) Mappable.findInstance(sourceBased),
            StorableSpringJpa.findInstance(sourceBased)
        );
    }

    @Override
    protected S findStorable() {
        return StorableSpringJpa.findInstance(setupRequiredWhenByService());
    }
}