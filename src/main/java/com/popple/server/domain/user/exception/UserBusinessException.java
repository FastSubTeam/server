package com.popple.server.domain.user.exception;

public class UserBusinessException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public UserBusinessException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
        this.message = errorCode.getErrorMessage();
    }
}
