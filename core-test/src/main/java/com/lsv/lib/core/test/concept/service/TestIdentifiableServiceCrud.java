package com.lsv.lib.core.test.concept.service;

import com.lsv.lib.core.behavior.Crud;
import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.dto.Filter;
import com.lsv.lib.core.concept.dto.ListDto;
import com.lsv.lib.core.concept.repository.Repository;
import com.lsv.lib.core.concept.service.Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

public interface TestIdentifiableServiceCrud<D extends Identifiable<?>, S extends Service<D> & Crud<D>, R extends Repository<D> & Crud<D>> extends TestIdentifiableServiceWithRepository<D, S, R> {

    @Test
    default public void insert() {
        R repositoryMock = repositoryMock();

        lenient().when(repositoryMock.create(any()))
            .thenAnswer(args -> newObjectWithId());

        D obj = newObjectCompleteWithoutId();
        Assertions.assertNotNull(service(repositoryMock).create(obj).getId());
    }

    @Test
    default public void update() {
        R repositoryMock = repositoryMock();

        lenient().when(repositoryMock.update(any()))
            .thenAnswer(AdditionalAnswers.returnsFirstArg());

        D obj = newObjectComplete();
        Assertions.assertEquals(obj, service(repositoryMock).update(obj));
    }

    @Test
    default public void delete() {
        Assertions.assertDoesNotThrow(() -> service(repositoryMock()).delete(newObjectWithId()));
    }

    @Test
    default public void findById() {
        R repositoryMock = repositoryMock();
        D obj = newObjectWithId();

        lenient().when(repositoryMock.findById(any()))
            .thenAnswer(invocation -> Optional.of(obj));

        Assertions.assertEquals(obj, service(repositoryMock).findById(obj).get());
    }

    @Test
    default public void findByFilters() {
        R repositoryMock = repositoryMock();
        D obj = newObjectComplete();
        Filter<D> filter = Filter.of(obj).get();

        lenient().when(repositoryMock.findByFilter(filter))
            .thenAnswer(args -> ListDto.of(List.of(obj)).get());

        Assertions.assertEquals(1, service(repositoryMock).findByFilter(filter).size());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    default void acessRepositoryDefault() {
        serviceImpl().repository();
    }

    D newObjectCompleteWithoutId();
    D newObjectComplete();
    D newObjectWithId();
}