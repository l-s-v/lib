package com.lsv.lib.core.test.concept.repository;

import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.helper.HelperClass;
import com.lsv.lib.core.pattern.register.RegisterByInterface;
import com.lsv.lib.core.test.TestForFactory;

public interface TestRepository<R extends Repository<?>> extends TestForFactory {

    @SuppressWarnings("unchecked")
    default R repository() {
        return (R) RegisterByInterface.findImplementation(
            HelperClass.identifyGenericsClass(this, Repository.class));
    }
}