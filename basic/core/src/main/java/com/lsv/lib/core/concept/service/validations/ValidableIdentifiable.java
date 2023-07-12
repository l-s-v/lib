package com.lsv.lib.core.concept.service.validations;

import com.lsv.lib.core.behavior.Identifiable;
import com.lsv.lib.core.exception.message.MessageDisplayExceptionEnum;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ValidableIdentifiable<T extends Identifiable<?>> implements Validable<T> {

    @Override
    public void validate(T identifiable, TypeOperation typeOperation) {
        switch (typeOperation) {
            case CREATE -> createIdNotPermit(identifiable);
            case UPDATE, DELETE, READ -> updateDeleteFindByIdRequiredId(identifiable);
        }
    }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private void createIdNotPermit(T identifiable) {
        if (identifiable.getId() != null) {
            throw MessageDisplayExceptionEnum.CREATE_ID_NOT_PERMIT.displayException();
        }
    }

    private void updateDeleteFindByIdRequiredId(T identifiable) {
        if (identifiable.getId() == null) {
            throw MessageDisplayExceptionEnum.UPDATE_DELETE_FIND_BY_ID_REQUIRED_ID.displayException();
        }
    }
}