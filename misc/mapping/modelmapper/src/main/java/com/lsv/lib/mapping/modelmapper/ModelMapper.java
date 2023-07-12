package com.lsv.lib.mapping.modelmapper;

import com.lsv.lib.core.mapper.Mappable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Mapping solution using ModelMapper.
 *
 * @author Leandro da Silva Vieira
 */
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
public class ModelMapper<S, D> implements Mappable<S, D> {

    private org.modelmapper.ModelMapper modelMapperComponent;
    private Class<S> sourceClass;
    private Class<D> destinationClass;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    public D to(S source) {
        return source == null ? null: modelMapperComponent().map(source, destinationClass());
    }

    @Override
    public S of(D destination) {
        return destination == null ? null: modelMapperComponent().map(destination, sourceClass());
    }
}