package com.lsv.lib.core.behavior;

import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.core.pattern.register.RegisterByInterface;
import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;

public interface Mappable<S, D> {

    Mappable<S, D> setup(Class<S> sourceClass, Class<D> destinationClass);

    D to(S source);

    S of(D destination);

    Class<S> sourceClass();

    Class<D> destinationClass();

    default List<D> to(@NonNull List<S> sources) {
        return sources.stream().map(this::to).collect(Collectors.toList());
    }

    default List<S> of(@NonNull List<D> destinations) {
        return destinations.stream().map(this::of).collect(Collectors.toList());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    static Mappable<?, ?> findInstance(Object sourceBase, Class<?> sourceExtends, Class<?> destinationExtends) {
        return findInstance(
            HelperClass.identifyGenericsClass(sourceBase, sourceExtends),
            HelperClass.identifyGenericsClass(sourceBase, destinationExtends)
        );
    }

    @SuppressWarnings("unchecked")
    static <S, D> Mappable<S, D> findInstance(Class<S> sourceClass, Class<D> destinationClass) {
        return RegisterByInterface.findImplementation(Mappable.class)
            .setup(sourceClass, destinationClass);
    }
}