package com.lsv.lib.core.exception.message;

import lombok.Getter;

/**
 * @author Leandro da Silva Vieira
 */
@Getter
public enum MessageDisplayExceptionEnum implements MessageDisplayException {
// Global
    SERVER_ERROR,
    BEAN_VALIDATION,
    MESSAGE_NOT_FOUND,
// Business
    ID_NOT_FOUND,
    CREATE_ID_NOT_PERMIT,
    UPDATE_DELETE_FIND_BY_ID_REQUIRED_ID,
    ;
}