package com.lsv.lib.core.msg;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Leandro da Silva Vieira
 */
@Getter
@AllArgsConstructor
public enum MsgsLib implements Msg {
    ID_NOT_FOUND,
    CREATE_ID_NOT_PERMIT,
    UPDATE_DELETE_FIND_BY_ID_REQUIRED_ID,
    ;
}