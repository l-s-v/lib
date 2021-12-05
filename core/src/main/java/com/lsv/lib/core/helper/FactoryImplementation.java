package com.lsv.lib.core.helper;

import com.lsv.lib.core.behavior.Mappable;
import com.lsv.lib.core.concept.repository.RepositoryProvider;
import com.lsv.lib.core.pattern.register.RegisterByInterface;

public final class FactoryImplementation {

    @SuppressWarnings("unchecked")
    public static <S, D> Mappable<S, D> createMapper(Class<S> sourceClass, Class<D> destinationClass) {
        return RegisterByInterface.findImplementation(Mappable.class)
            .setup(sourceClass, destinationClass);
    }

    public static RepositoryProvider<?, ?, ?, ?> createProviderRepository(Object source) {
        return RegisterByInterface.findImplementation(RepositoryProvider.class)
            .setup(source);
    }
}