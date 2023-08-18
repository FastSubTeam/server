package com.popple.server.domain.user.exception;

public class UserBadRequestException extends UserBusinessException {
    public UserBadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
