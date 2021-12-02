package com.lsv.lib.core.concept.service.validations;

import lombok.NonNull;

import java.util.List;

public interface Validable<T> {

    Validable<T> objValidable(T objValidable);
    Validable<T> typeOperation(TypeOperation typeOperation);
    TypeOperation typeOperation();

    default boolean operationIs(@NonNull TypeOperation ... typeOperations) {
        return List.of(typeOperations).contains(this.typeOperation());
    }
}