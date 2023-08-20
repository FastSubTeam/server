package com.popple.server.domain.user.exception;

public class AlreadySignUpException extends UserBadRequestException {
    public AlreadySignUpException(ErrorCode errorCode) {
        super(errorCode);
    }
}
