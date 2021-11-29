package com.lsv.lib.core;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.concept.dto.ListDto;
import com.lsv.lib.core.helper.HelperClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class HelperClassTest {

    @Test
    public void identifyGenericsClassNoSuchElement() {
        Assertions.assertThrows(NoSuchElementException.class, () ->
            HelperClass.identifyGenericsClass(ListDto.of(new ArrayList<>()).get(), List.class));
    }

    @Test
    public void identifyGenericsClassSpecifiedTyped() {
        Assertions.assertEquals(Long.class, HelperClass.identifyGenericsClass(new Identifiable<Long>() {
            @Override
            public Long id() {
                return null;
            }
        }, Number.class));
    }

    @Test
    public void identifyGenericsClass() {
        Assertions.assertEquals(String.class, HelperClass.identifyGenericsClass(new Identifiable<String>() {
            @Override
            public String id() {
                return null;
            }
        }));
    }
}
