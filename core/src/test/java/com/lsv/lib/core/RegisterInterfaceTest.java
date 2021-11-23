package com.lsv.lib.core;

import com.lsv.lib.core.mock.Implementation;
import com.lsv.lib.core.mock.InterfaceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RegisterInterfaceTest {

    @Test
    public void registroAutomatico() {
        Assertions.assertInstanceOf(Implementation.class,
                RegisterInterface.create(InterfaceTest.class)
                        .pesquisarImplementacoesModularizadas()
                        .implementacoes()
                        .toArray()[0]);
    }

    @Test
    public void registroManual() {
        Assertions.assertInstanceOf(Implementation.class,
                RegisterInterface.create(InterfaceTest.class)
                        .registrar(new Implementation())
                        .implementacoes()
                        .toArray()[0]);
    }
}