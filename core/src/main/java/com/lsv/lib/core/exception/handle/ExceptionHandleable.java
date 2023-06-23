package com.lsv.lib.core.exception.handle;

import com.lsv.lib.core.exception.ProblemDetail;

import java.io.Serializable;

/**
 * @author Leandro da Silva Vieira
 */
public interface ExceptionHandleable<T extends Throwable> extends Serializable {

    ProblemDetail create(T throwable);
}