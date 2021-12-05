package com.lsv.lib.core;

import com.lsv.lib.core.concept.dto.Dto;
import com.lsv.lib.core.concept.dto.ListDto;
import com.lsv.lib.core.concept.repository.RepositoryImplementeable;
import com.lsv.lib.core.concept.repository.RepositoryProvider;
import com.lsv.lib.core.helper.RepositoryProviderImpl;
import com.lsv.lib.core.pattern.register.RegisterByInterface;
import com.lsv.lib.core.pattern.register.mock.Implementation;
import com.lsv.lib.core.pattern.register.mock.InterfaceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

public class RegisterByInterfaceTest {

    @Test
    public void automaticRegistryByService() {
        Assertions.assertEquals(1,
                RegisterByInterface.of(RepositoryProvider.class)
                        .findImplementationsByService()
                        .implementations()
                        .size());
    }

    @Test
    public void automaticRegistryByServiceNotFound() {
        Assertions.assertEquals(0,
            RegisterByInterface.of(InterfaceTest.class)
                .findImplementationsByService()
                .implementations()
                .size());
    }

    @Test
    public void automaticRegistryByReflection() {
        Assertions.assertEquals(1,
            RegisterByInterface.of(RepositoryProvider.class)
                .findImplementationsByReflection(RepositoryProviderImpl.class.getPackageName())
                .implementations()
                .size());
    }

    @Test
    public void automaticRegistryByReflectionNotFound() {
        Assertions.assertEquals(0,
                RegisterByInterface.of(RepositoryImplementeable.class)
                        .findImplementationsByReflection(RepositoryImplementeable.class.getPackageName())
                        .implementations()
                        .size());
    }

    @Test
    public void automaticRegistryByReflectionUnsupportedOperationException() {
        Assertions.assertThrows(UnsupportedOperationException.class, () ->
            RegisterByInterface.of(Dto.class)
                .findImplementationsByReflection(ListDto.class.getPackageName()));
    }

    @Test
    public void manualRegistry() {
        Assertions.assertInstanceOf(Implementation.class,
                RegisterByInterface.of(InterfaceTest.class)
                        .register(new Implementation())
                        .implementations()
                        .toArray()[0]);
    }

    @Test
    public void invalidArgument() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                RegisterByInterface.of(Implementation.class)
                        .implementations());
    }

    @Test
    public void findImplementationNoSuchElement() {
        Assertions.assertThrows(NoSuchElementException.class, () ->
                RegisterByInterface.findImplementation(List.class));
    }
}