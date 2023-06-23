package com.lsv.lib.spring.web.test.controller;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.concept.dto.Filter;
import com.lsv.lib.core.test.TestForFactory;
import com.lsv.lib.core.test.helper.HelperDynamicTest;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.function.Executable;

import java.util.stream.Stream;

public interface TestControllerReadable<
        IN extends Identifiable<?>,
        OUT extends Identifiable<?>,
        S extends Readable<OUT>>
        extends
        TestForFactory {

    @Override
    default Stream<DynamicNode> of() {
        return HelperDynamicTest.joinAndRemoveDuplicatedByName(
                Stream.of(
                        findById(),
                        findByIdNoFound(),
                        findByFilterWithoutParams(),
                        findByFilterWithParamsPageable()),
                TestForFactory.super.of());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    Executable execFindByIde();

    Executable execFindByIdNoFound();

    Executable execFindByFilterWithoutParams();

    Executable execFindByFilterWithParamsPageable();

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    static Filter<?> createFilter() {
        return Filter.of(null)
                .page(Filter.Page.of()
                        .numPage(2)
                        .size(20)
                        .get())
                .orderBy(Filter.OrderBy.of()
                        .property("nome")
                        .asc(false)
                        .get())
                .get();
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private DynamicTest findById() {
        return DynamicTest.dynamicTest("findById", execFindByIde());
    }

    private DynamicTest findByIdNoFound() {
        return DynamicTest.dynamicTest("findByIdNoFound", execFindByIdNoFound());
    }

    private DynamicTest findByFilterWithoutParams() {
        return DynamicTest.dynamicTest("findByFilterWithoutParams", () -> execFindByFilterWithoutParams());
    }

    private DynamicTest findByFilterWithParamsPageable() {
        return DynamicTest.dynamicTest("findByFilterWithParamsPageable", execFindByFilterWithParamsPageable());
    }
}