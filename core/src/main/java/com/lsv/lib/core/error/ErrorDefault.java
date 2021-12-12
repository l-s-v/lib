package com.lsv.lib.core.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter(AccessLevel.PRIVATE)
public class ErrorDefault implements Error<Throwable> {

    private String message;

    @Override
    public Error<Throwable> create(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);

        return setMessage(throwable.getMessage());
    }
}