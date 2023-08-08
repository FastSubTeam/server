package com.popple.server.domain.user.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserErrorCode implements ErrorCode {

    EXIST_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    PROCEEDING_EMAIL(HttpStatus.BAD_REQUEST, "이미 회원가입을 진행중인 이메일입니다"),
    NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다"),
    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    UserErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
    }
