package org.pamdesa.model.exception;

import org.pamdesa.model.enums.ErrorCode;

public class ClientException extends RuntimeException {

    public ClientException(ErrorCode errorCode) {
        super(errorCode.name());
    }

}
