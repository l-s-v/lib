package com.lsv.lib.core.helper;

import com.lsv.lib.core.behavior.Identifiable;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Function;

public class HelperClassTest {

    @Test
    public void identifyGenericsClassNoSuchElement() {
        Assertions.assertThrows(NoSuchElementException.class, () -> HelperClass.identifyGenericsClass(new Function<String, Long>() {
            @Override
            public Long apply(String s) {
                return null;
            }
        }, 2));
    }

    @Test
    public void identifyGenericsClassSpecifiedTyped() {
        Assertions.assertEquals(Long.class, HelperClass.identifyGenericsClass(new Identifiable<Long>() {
            @Override
            public Long getId() {
                return null;
            }
            @Override
            public Identifiable<Long> setId(Long aLong) {
                return null;
            }
        }, Number.class));
    }

    @Test
    public void identifyGenericsClass() {
        Assertions.assertEquals(String.class, HelperClass.identifyGenericsClass(new Identifiable<String>() {
            @Override
            public String getId() {
                return null;
            }
            @Override
            public Identifiable<String> setId(String s) {
                return null;
            }
        }));
    }

    @Test
    public void identifyGenericsClassPositionType() {
        Assertions.assertEquals(Long.class, HelperClass.identifyGenericsClass(new Function<String, Long>() {
            @Override
            public Long apply(String s) {
                return null;
            }
        }, 1));
    }

    @Test
    public void testCloneSerializable() {
        Source source = new Source()
            .id(UUID.randomUUID())
            .nome("nome")
            .descricao("descrição");

        Source destination = HelperClass.cloneSerializable(source);

        Assertions.assertEquals(source.id(), destination.id());
        Assertions.assertEquals(source.nome(), destination.nome());
        Assertions.assertEquals(source.descricao(), destination.descricao());
        Assertions.assertNotEquals(source, destination);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Getter
    @Setter
    private static class Source implements Serializable {
        private UUID id;
        private String nome;
        private String descricao;
    }
}