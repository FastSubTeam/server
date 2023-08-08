package com.popple.server.domain.user.exception;


import io.jsonwebtoken.JwtException;
import lombok.Getter;

@Getter
public class InvalidJwtTokenException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;

    public InvalidJwtTokenException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
        this.message = errorCode.getErrorMessage();
    }
}
