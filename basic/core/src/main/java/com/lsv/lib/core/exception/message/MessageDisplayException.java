package com.lsv.lib.core.exception.message;

import com.lsv.lib.core.exception.DisplayException;
import com.lsv.lib.core.exception.helper.HelperExceptionHandler;

/**
 * Sets the default for messages.
 *
 * @author Leandro da Silva Vieira
 */
public interface MessageDisplayException {

    String name();

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    default DisplayException displayException(Object... detailMessageArguments) {
        return displayException((Throwable) null, detailMessageArguments);
    }

    default DisplayException displayException(Integer status, Object... detailMessageArguments) {
        return displayException(null, status, detailMessageArguments);
    }

    default DisplayException displayException(Throwable cause, Object... detailMessageArguments) {
        return displayException(cause, null, detailMessageArguments);
    }

    default DisplayException displayException(Throwable cause, Integer status, Object... detailMessageArguments) {
        return new DisplayException(
                HelperExceptionHandler.createProblemDetail(
                        this,
                        null,
                        status,
                        detailMessageArguments),
                cause);
    }
}