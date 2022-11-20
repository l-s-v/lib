package com.lsv.lib.core.loader;

import com.lsv.lib.core.concept.dto.Dto;
import com.lsv.lib.core.concept.dto.ListDto;
import com.lsv.lib.core.concept.repository.RepositoryImplementeable;
import com.lsv.lib.core.loader.mock.Implementation;
import com.lsv.lib.core.loader.mock.InterfaceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

public class LoaderTest {

    @Test
    public void automaticRegistryByServiceNotFound() {
        Assertions.assertEquals(0,
                Loader.of(InterfaceTest.class)
                        .findImplementationsByService()
                        .implementations()
                        .size());
    }

    @Test
    public void automaticRegistryByReflectionNotFound() {
        Assertions.assertEquals(0,
                Loader.of(RepositoryImplementeable.class)
                        .findImplementationsByReflection(RepositoryImplementeable.class.getPackageName())
                        .implementations()
                        .size());
    }

    @Test
    public void automaticRegistryByReflectionUnsupportedOperationException() {
        Assertions.assertThrows(UnsupportedOperationException.class, () ->
                Loader.of(Dto.class)
                        .findImplementationsByReflection(ListDto.class.getPackageName()));
    }

    @Test
    public void manualRegistry() {
        Assertions.assertInstanceOf(Implementation.class,
                Loader.of(InterfaceTest.class)
                        .register(new Implementation())
                        .implementations()
                        .toArray()[0]);
    }

    @Test
    public void invalidArgument() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                Loader.of(Implementation.class)
                        .implementations());
    }

    @Test
    public void findImplementationNoSuchElement() {
        Assertions.assertThrows(NoSuchElementException.class, () ->
                Loader.findImplementation(List.class));
    }
}