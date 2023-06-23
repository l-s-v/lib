package com.lsv.lib.core.msg;

import com.lsv.lib.core.exception.BusinessException;
import com.lsv.lib.core.exception.ProblemDetail;

/**
 * Sets the default for messages.
 *
 * @author Leandro da Silva Vieira
 */
public interface Msg {

    String name();

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    default BusinessException businessException(Object ... detailMessageArguments) {
        return new BusinessException(new ProblemDetail()
                .title(name())
                .detail(name())
                .detailMessageArguments(detailMessageArguments)
        );
    }

    default BusinessException businessExceptionWithStatus(int status, Object ... detailMessageArguments) {
        return new BusinessException(new ProblemDetail()
                .title(name())
                .detail(name())
                .status(status)
                .detailMessageArguments(detailMessageArguments)
        );
    }
}