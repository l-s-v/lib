package com.lsv.lib.mapping.modelmapper;

import com.lsv.lib.core.behavior.Mappable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
public class ModelMapper<S, D> implements Mappable<S, D> {

    private org.modelmapper.ModelMapper modelMapperComponent;
    private Class<S> sourceClass;
    private Class<D> destinationClass;

    @Override
    public Mappable<S, D> setup(Class<S> sourceClass, Class<D> destinationClass) {
        this.sourceClass(sourceClass);
        this.destinationClass(destinationClass);
        this.modelMapperComponent(new org.modelmapper.ModelMapper());
        return this;
    }

    @Override
    public D to(S source) {
        return this.modelMapperComponent().map(source, this.destinationClass());
    }

    @Override
    public S of(D destination) {
        return this.modelMapperComponent().map(destination, this.sourceClass());
    }
}