package com.popple.server.domain.user.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistException extends RuntimeException {
    private ErrorCode errorCode;

    public AlreadyExistException() {
        super();
    }

    public AlreadyExistException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

    public AlreadyExistException(String message) {
        super(message);
    }

    public AlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyExistException(Throwable cause) {
        super(cause);
    }

    protected AlreadyExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public HttpStatus getHttpStatus() {
        return errorCode.getHttpStatus();
    }
}
