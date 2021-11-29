package com.lsv.lib.core;

import com.lsv.lib.core.pattern.register.RegisterInterface;
import com.lsv.lib.core.pattern.register.mock.Implementation;
import com.lsv.lib.core.pattern.register.mock.InterfaceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

public class RegisterInterfaceTest {

    @Test
    public void automaticRegistry() {
        Assertions.assertEquals(0,
                RegisterInterface.of(InterfaceTest.class)
                        .findSubtypes()
                        .implementations()
                        .size());
    }

    @Test
    public void manualRegistry() {
        Assertions.assertInstanceOf(Implementation.class,
                RegisterInterface.of(InterfaceTest.class)
                        .register(new Implementation())
                        .implementations()
                        .toArray()[0]);
    }

    @Test
    public void invalidArgument() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                RegisterInterface.of(Implementation.class)
                        .implementations());
    }

    @Test
    public void findImplementationNoSuchElement() {
        Assertions.assertThrows(NoSuchElementException.class, () ->
                RegisterInterface.findImplementation(List.class));
    }
}