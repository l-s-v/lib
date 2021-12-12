package com.lsv.lib.core.concept.controller;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mappable;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.helper.HelperClass;
import lombok.*;

import java.util.Optional;

@RequiredArgsConstructor
@NoArgsConstructor
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
public class ControllerProviderBasicImpl<
    IN extends Identifiable<?>,
    OUT extends Identifiable<?>,
    S extends Service<OUT>>
    implements
    ControllerProvider<IN, OUT, S> {

    @NonNull
    private Mappable<IN, OUT> mappable;
    @NonNull
    private S service;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressWarnings("unchecked")
    @Override
    public ControllerProvider<IN, OUT, S> configureRequiredWhenByService(ControllerProvider<IN, OUT, S> sourceBase) {
        mappable(Optional.ofNullable(mappable).orElse((Mappable<IN, OUT>) findMapper(sourceBase)));
        service(Optional.ofNullable(service).orElse(findService()));
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public OUT mappableTo(IN identifiable) {
        if (mappable().sourceClass().equals(mappable().destinationClass())) {
            return (OUT) identifiable;
        }
        return mappable().to(identifiable);
    }

    @SuppressWarnings("unchecked")
    @Override
    public IN mappableOf(OUT identifiable) {
        if (mappable().sourceClass().equals(mappable().destinationClass())) {
            return (IN) identifiable;
        }
        return mappable().of(identifiable);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static <
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S extends Service<OUT>>
    ControllerProvider<IN, OUT, S> of(ControllerProvider<IN, OUT, S> sourceBase) {
        return new ControllerProviderBasicImpl<IN, OUT, S>().configureRequiredWhenByService(sourceBase);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    protected Mappable<?, ?> findMapper(ControllerProvider<IN, OUT, S> sourceBase) {
        return Mappable.findInstance(
            HelperClass.identifyGenericsClass(sourceBase, 0),
            HelperClass.identifyGenericsClass(sourceBase, 1));
    }

    @SuppressWarnings("unchecked")
    protected S findService() {
        return (S) service(Service.findInstance(this));
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
}