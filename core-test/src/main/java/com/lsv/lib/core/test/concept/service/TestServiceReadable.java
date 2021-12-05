package com.lsv.lib.core.test.concept.service;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.behavior.Readable;
import com.lsv.lib.core.concept.dto.Filter;
import com.lsv.lib.core.concept.dto.ListDto;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.Service;
import com.lsv.lib.core.test.helper.HelperDynamicTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

public interface TestServiceReadable
    <
        D extends Identifiable<?>,
        S extends Service<D> & Readable<D>,
        R extends Repository<D> & Readable<D>>
    extends
    TestServiceWithRepository<D, S, R>,
    TestServiceProvider<D> {

    @Override
    default Stream<DynamicNode> of() {
        return HelperDynamicTest.joinAndRemoveDuplicatedByName(
            Stream.of(
                this.findById(),
                this.findByFilters()),
            TestServiceWithRepository.super.of());
    }
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private DynamicNode findById() {
        return DynamicTest.dynamicTest("findById", () -> {
            R repositoryMock = repositoryMock();
            D obj = newObjectWithId();

            lenient().when(repositoryMock.findById(any()))
                .thenAnswer(invocation -> Optional.of(obj));

            Assertions.assertEquals(obj, service(repositoryMock).findById(obj).get());
        });
    }

    private DynamicNode findByFilters() {
        return DynamicTest.dynamicTest("findByFilters", () -> {
            R repositoryMock = repositoryMock();
            D obj = newObjectComplete();
            Filter<D> filter = Filter.of(obj).get();

            lenient().when(repositoryMock.findByFilter(filter))
                .thenAnswer(args -> ListDto.of(List.of(obj)).get());

            Assertions.assertEquals(1, service(repositoryMock).findByFilter(filter).size());
        });
    }
}