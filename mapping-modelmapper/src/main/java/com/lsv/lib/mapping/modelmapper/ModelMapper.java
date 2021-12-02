package com.lsv.lib.mapping.modelmapper;

import com.lsv.lib.core.behavior.Mapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class ModelMapper<S, D> implements Mapper<S, D> {

    private org.modelmapper.ModelMapper modelMapperComponent;
    private Class<S> sourceClass;
    private Class<D> destinationClass;

    @Override
    public Mapper<S, D> setup(Class<S> sourceClass, Class<D> destinationClass) {
        this.setSourceClass(sourceClass);
        this.setDestinationClass(destinationClass);
        this.setModelMapperComponent(new org.modelmapper.ModelMapper());
        return this;
    }

    @Override
    public D to(S source) {
        return this.modelMapperComponent.map(source, this.getDestinationClass());
    }

    @Override
    public S of(D destination) {
        return this.modelMapperComponent.map(destination, this.getSourceClass());
    }
}