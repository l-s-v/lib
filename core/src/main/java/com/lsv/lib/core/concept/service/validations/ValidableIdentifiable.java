package com.lsv.lib.core.concept.service.validations;

import com.lsv.lib.core.behavior.Identifiable;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(builderMethodName = "of", buildMethodName = "get")
public class ValidableIdentifiable<
        T extends Identifiable<?>>
        implements Validable<T> {

    public static final String MSG_CREATE_ID_NOT_PERMIT = "Id deve ser nulo";
    public static final String MSG_UPDATE_DELETE_FIND_BY_ID_REQUIRED_ID = "Id deve ser informado";

    @NotNull
    private T objValidable;
    private TypeOperation typeOperation;

    @AssertFalse(message = MSG_CREATE_ID_NOT_PERMIT)
    private boolean isCreateIdNotPermit() {
        return
                operationIs(TypeOperation.CREATE) &&
                        objValidable().getId() != null;
    }

    @AssertFalse(message = MSG_UPDATE_DELETE_FIND_BY_ID_REQUIRED_ID)
    private boolean isUpdateDeleteFindByIdRequiredId() {
        return
                operationIs(TypeOperation.UPDATE, TypeOperation.DELETE, TypeOperation.READ) &&
                        objValidable().getId() == null;
    }
}