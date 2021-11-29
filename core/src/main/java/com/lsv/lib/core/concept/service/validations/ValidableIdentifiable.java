package com.lsv.lib.core.concept.service.validations;

import com.lsv.lib.core.behavior.Identifiable;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder(builderMethodName = "of", buildMethodName = "get")
public class ValidableIdentifiable<T extends Identifiable<?>> implements Validable<T> {

    public static final String MSG_CREATE_ID_NOT_PERMIT = "Id deve ser nulo";
    public static final String MSG_UPDATE_DELETE_FIND_BY_ID_REQUIRED_ID = "Id deve ser informado";

    @NotNull
    private T objValidable;
    private TypeOperation typeOperation;

    @AssertFalse(message = MSG_CREATE_ID_NOT_PERMIT)
    private boolean isCreateIdNotPermit() {
        return
            TypeOperation.CREATE.equals(typeOperation()) &&
            objValidable().id() != null;
    }

    @AssertFalse(message = MSG_UPDATE_DELETE_FIND_BY_ID_REQUIRED_ID)
    private boolean isUpdateDeleteFindByIdRequiredId() {
        return
            List.of(TypeOperation.UPDATE, TypeOperation.DELETE, TypeOperation.READ).contains(typeOperation()) &&
            objValidable().id() == null;
    }
}