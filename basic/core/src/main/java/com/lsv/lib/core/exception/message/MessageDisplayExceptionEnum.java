package com.lsv.lib.core.exception.message;

import com.lsv.lib.core.exception.helper.ProblemDetail;
import lombok.Getter;

/**
 * @author Leandro da Silva Vieira
 */
@Getter
public enum MessageDisplayExceptionEnum implements MessageDisplayException {
    // Global
    SERVER_ERROR,
    BEAN_VALIDATION,
    FORBIDDEN,
    UNAUTHORIZED,
    NOT_FOUND,
    MESSAGE_NOT_FOUND,
    // Business
    ID_NOT_FOUND,
    CREATE_ID_NOT_PERMIT,
    UPDATE_DELETE_FIND_BY_ID_REQUIRED_ID,
    ;

    public static MessageDisplayException valueOf(int status) {
        return switch (status) {
            case ProblemDetail.NOT_FOUND -> NOT_FOUND;
            case ProblemDetail.UNAUTHORIZED -> UNAUTHORIZED;
            case ProblemDetail.FORBIDDEN -> FORBIDDEN;
            default -> SERVER_ERROR;
        };
    }
}