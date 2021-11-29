package com.lsv.lib.core.concept.service.validations;

public interface Validable<T> {

    Validable<T> objValidable(T objValidable);
    Validable<T> typeOperation(TypeOperation typeOperation);
}