package com.lsv.lib.core.concept.service.validations;

public interface Validable<T> {

    void validate(T identifiable, TypeOperation typeOperation);
}