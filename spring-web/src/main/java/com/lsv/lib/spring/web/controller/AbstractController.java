package com.lsv.lib.spring.web.controller;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Mappable;
import com.lsv.lib.core.concept.controller.Controller;
import com.lsv.lib.core.concept.controller.ControllerProvider;
import com.lsv.lib.core.concept.controller.ControllerProviderBasicImpl;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.spring.core.SpringFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.NoSuchElementException;
import java.util.Optional;

@Setter(AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor
@SuperBuilder(builderMethodName = "of", buildMethodName = "get")
public abstract class AbstractController<
    IN extends Identifiable<?>,
    OUT extends Identifiable<?>,
    S extends Service<OUT>>
    implements
    Controller<IN, OUT, S> {

    private ControllerProvider<IN, OUT, S> controllerProvider;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public ControllerProvider<IN, OUT, S> controllerProvider() {
        return Optional.ofNullable(controllerProvider)
            .orElse(createControllerProvider());
    }

    protected ControllerProvider<IN, OUT, S> createControllerProvider() {
        controllerProvider(new ControllerProviderBasicImpl<>(
            Mappable.findInstance(
                HelperClass.identifyGenericsClass(this, 0),
                HelperClass.identifyGenericsClass(this, 1)
            ),
            createService()
        ));
        return controllerProvider;
    }

    @SuppressWarnings("unchecked")
    protected S createService() {
        try {
            return (S) SpringFactory.bean(this, Service.class);
        } catch (NoSuchElementException e) {
            return Service.findInstance(this);
        }
    }
}