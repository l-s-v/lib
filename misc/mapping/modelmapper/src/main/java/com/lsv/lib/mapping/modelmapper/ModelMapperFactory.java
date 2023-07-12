package com.lsv.lib.mapping.modelmapper;

import com.google.auto.service.AutoService;
import com.lsv.lib.core.annotation.Priority;
import com.lsv.lib.core.mapper.Mappable;
import com.lsv.lib.core.mapper.MappableFactory;
import lombok.NonNull;
import org.reflections.util.ClasspathHelper;

/**
 * Factory for mapping between objects using ModelMapper.
 *
 * @author Leandro da Silva Vieira
 */
@Priority(1)
@AutoService(MappableFactory.class)
public class ModelMapperFactory implements MappableFactory {

    @Override
    public <S, D> Mappable<S, D> create(@NonNull Class<S> sourceClass, @NonNull Class<D> destinationClass) {
        return new ModelMapper<>(createMapper(), sourceClass, destinationClass);
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private org.modelmapper.ModelMapper createMapper() {
        var mapper = new org.modelmapper.ModelMapper();
        mapper.getConfiguration().setPropertyCondition(context -> verifyNotLazy(context.getSource()));
        return mapper;
    }

    private boolean verifyNotLazy(Object source) {
        if (source == null) {
            return true;
        }
        try {
            var classLazy = ClasspathHelper.contextClassLoader().loadClass("org.hibernate.collection.spi.LazyInitializable");

            return !classLazy.isAssignableFrom(source.getClass()) ||
                    (boolean) classLazy.getMethod("wasInitialized").invoke(source);
        } catch (Exception e) {
            return true;
        }
    }
}