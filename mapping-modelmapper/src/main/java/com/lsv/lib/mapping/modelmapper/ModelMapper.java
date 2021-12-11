package com.lsv.lib.mapping.modelmapper;

import com.lsv.lib.core.behavior.Mappable;
import lombok.*;

@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
public class ModelMapper<S, D> implements Mappable<S, D> {

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
        modelMapperComponent(new org.modelmapper.ModelMapper());
        return this;
    }

    @Override
    public D to(S source) {
        return modelMapperComponent().map(source, destinationClass());
    }

    @Override
    public S of(D destination) {
        return modelMapperComponent().map(destination, sourceClass());
    }
}