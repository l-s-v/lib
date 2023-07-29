package com.lsv.lib.core.loader;

import com.lsv.lib.core.behavior.Deletable;
import com.lsv.lib.core.concept.dto.Dto;
import com.lsv.lib.core.concept.dto.ListDto;
import com.lsv.lib.core.loader.mock.Implementation;
import com.lsv.lib.core.loader.mock.InterfaceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Leandro da Silva Vieira
 */
public class LoaderTest {

    @Test
    public void automaticRegistryByServiceNotFound() {
        Assertions.assertEquals(0,
                Loader.of(InterfaceTest.class)
                        .findImplementationsByFirstLoader()
                        .size());
    }

    @Test
    public void automaticRegistryByReflectionNotFound() {
        Assertions.assertEquals(0,
                Loader.of(Deletable.class)
                        .findImplementationsByReflection(Deletable.class.getPackageName())
                        .size());
    }

    @Test
    public void automaticRegistryByReflectionUnsupportedOperationException() {
        Assertions.assertThrows(UnsupportedOperationException.class, () ->
                Loader.of(Dto.class)
                        .findImplementationsByReflection(ListDto.class.getPackageName()));
    }

    @Test
    public void findImplementationNoSuchElement() {
        Assertions.assertThrows(NoSuchElementException.class, () ->
                Loader.of(List.class).findUniqueImplementationByFirstLoader());
    }
}