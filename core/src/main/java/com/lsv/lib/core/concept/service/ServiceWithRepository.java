package com.lsv.lib.core.concept.service;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.validations.Validable;
import com.lsv.lib.core.concept.service.validations.ValidableIdentifiable;
import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.core.pattern.register.RegisterByInterface;

import java.util.List;

public interface ServiceWithRepository<
    I extends Identifiable<?>,
    R extends Repository<I>>
    extends
    Service<I> {

    @SuppressWarnings({"unchecked"})
    default List<Validable<I>> validables() {
        return List.of((Validable<I>) ValidableIdentifiable.of().get());
    }

    @SuppressWarnings({"unchecked"})
    default R repository() {
        return (R) RegisterByInterface.findImplementation(
            HelperClass.identifyGenericsClass(this, Repository.class));
    }

    ServiceWithRepository<I, R> repository(R repository);
}