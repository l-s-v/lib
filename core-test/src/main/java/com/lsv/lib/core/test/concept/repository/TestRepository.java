package com.lsv.lib.core.test.concept.repository;

import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.core.pattern.register.RegisterInterface;
import org.junit.jupiter.api.DynamicTest;

import java.util.stream.Stream;

public interface TestRepository<R extends Repository<?>> {

    default Stream<DynamicTest> of() {
        return Stream.empty();
    }

    @SuppressWarnings("unchecked")
    default R repository() {
        return (R) RegisterInterface.findImplementation(
            HelperClass.identifyGenericsClass(this, Repository.class));
    }
}