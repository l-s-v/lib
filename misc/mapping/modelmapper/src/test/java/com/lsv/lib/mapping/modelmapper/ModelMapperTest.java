package com.lsv.lib.mapping.modelmapper;

import com.lsv.lib.core.mapper.HelperMappable;
import com.lsv.lib.core.mapper.Mappable;
import com.lsv.lib.mapping.modelmapper.test.Destination;
import com.lsv.lib.mapping.modelmapper.test.Source;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class ModelMapperTest {

    @Test
    public void toSuccess() {
        Source source = new Source()
                .setId(UUID.randomUUID())
                .setNome("nome")
                .setDescricao("descrição");

        Destination destination = mapper().to(source);
        Assertions.assertEquals(source.getId(), destination.getId());
        Assertions.assertEquals(source.getNome(), destination.getNome());
    }

    @Test
    public void ofSuccess() {
        Destination destination = new Destination()
                .setId(UUID.randomUUID())
                .setNome("nome");

        Source source = mapper().of(destination);
        Assertions.assertEquals(source.getId(), destination.getId());
        Assertions.assertEquals(source.getNome(), destination.getNome());
        Assertions.assertNull(source.getDescricao());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressWarnings({"unchecked"})
    private Mappable<Source, Destination> mapper() {
        return HelperMappable.of(Source.class, Destination.class);
    }
}
