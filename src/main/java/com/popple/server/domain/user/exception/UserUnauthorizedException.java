package com.popple.server.domain.user.exception;

public class UserUnauthorizedException extends UserBusinessException{
    public UserUnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
