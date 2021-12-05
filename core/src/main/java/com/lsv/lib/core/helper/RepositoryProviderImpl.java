package com.lsv.lib.core.helper;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mappable;
import com.lsv.lib.core.behavior.Persistable;
import com.lsv.lib.core.behavior.Storable;
import com.lsv.lib.core.concept.repository.RepositoryProvider;
import com.lsv.lib.core.pattern.register.RegisterInterface;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.Serializable;

public class RepositoryProviderImpl<
    I extends Identifiable<ID>,
    ID extends Serializable,
    P extends Persistable<ID>,
    S>
    implements RepositoryProvider<I, ID, P, S> {

    private Mappable<I, P> mappable;
    private S storable;
    @Getter(AccessLevel.PROTECTED)
    private Object sourceToFindGenerics;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressWarnings("unchecked")
    @Override
    public Mappable<I, P> mappable() {
        if (mappable == null) {
            mappable((Mappable<I, P>) findMapper());
        }
        return mappable;
    }

    @Override
    public RepositoryProvider<I, ID, P, S> mappable(Mappable<I, P> mappable) {
        this.mappable = mappable;
        return this;
    }

    @Override
    public S storable() {
        if (storable == null) {
            storable(findStorable());
        }
        return storable;
    }

    @Override
    public RepositoryProvider<I, ID, P, S> storable(S storable) {
        this.storable = storable;
        return this;
    }

    @Override
    public RepositoryProvider<I, ID, P, S> setup(Object source) {
        this.sourceToFindGenerics = source;
        return this;
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    protected Mappable<?, ?> findMapper() {
        return FactoryImplementation.createMapper(
            HelperClass.identifyGenericsClass(sourceToFindGenerics, Identifiable.class),
            HelperClass.identifyGenericsClass(sourceToFindGenerics, Persistable.class)
        );
    }

    @SuppressWarnings("unchecked")
    protected S findStorable() {
        return (S) RegisterInterface.findImplementation(Storable.class);
    }
}