package com.lsv.lib.mapping.modelmapper;

import com.google.auto.service.AutoService;
import com.lsv.lib.core.behavior.Mappable;
import lombok.*;
import org.modelmapper.Condition;
import org.modelmapper.spi.MappingContext;
import org.reflections.util.ClasspathHelper;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@AutoService(Mappable.class)
public class ModelMapper<S, D> implements Mappable<S, D> {

    @Getter(AccessLevel.PRIVATE)
    private org.modelmapper.ModelMapper modelMapperComponent;
    @NonNull
    private Class<S> sourceClass;
    @NonNull
    private Class<D> destinationClass;

    public ModelMapper(@NonNull Class<S> sourceClass, @NonNull Class<D> destinationClass) {
        setup(sourceClass, destinationClass);
    }

    @Override
    public Mappable<S, D> setup(Class<S> sourceClass, Class<D> destinationClass) {
        sourceClass(sourceClass);
        destinationClass(destinationClass);
        modelMapperComponent(createMapper());

        return this;
    }

    @Override
    public D to(S source) {
        return source == null ? null: modelMapperComponent().map(source, destinationClass());
    }

    @Override
    public S of(D destination) {
        return destination == null ? null: modelMapperComponent().map(destination, sourceClass());
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private org.modelmapper.ModelMapper createMapper() {
        var mapper = new org.modelmapper.ModelMapper();

        mapper.getConfiguration().setPropertyCondition(new Condition<Object, Object>() {
            public boolean applies(MappingContext<Object, Object> context) {
                return verifyNotLazy(context.getSource());
            }
        });

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