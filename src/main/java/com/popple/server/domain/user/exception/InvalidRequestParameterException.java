package com.popple.server.domain.user.exception;


import lombok.Getter;

@Getter
public class InvalidRequestParameterException extends UserBadRequestException {
    public InvalidRequestParameterException(ErrorCode errorCode) {
        super(errorCode);
    }
}
