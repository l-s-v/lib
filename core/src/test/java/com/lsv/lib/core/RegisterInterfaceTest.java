package com.lsv.lib.core;

import com.lsv.lib.core.pattern.register.RegisterInterface;
import com.lsv.lib.core.pattern.register.mock.Implementation;
import com.lsv.lib.core.pattern.register.mock.InterfaceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RegisterInterfaceTest {

    @Test
    public void automaticRegistry() {
        Assertions.assertInstanceOf(Implementation.class,
                RegisterInterface.of(InterfaceTest.class)
                        .findSubtypes()
                        .subTypes()
                        .toArray()[0]);
    }

    @Test
    public void manualRegistry() {
        Assertions.assertInstanceOf(Implementation.class,
                RegisterInterface.of(InterfaceTest.class)
                        .register(new Implementation())
                        .subTypes()
                        .toArray()[0]);
    }

    @Test
    public void invalidArgument() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                RegisterInterface.of(Implementation.class)
                        .register(new Implementation())
                        .subTypes());
    }
}