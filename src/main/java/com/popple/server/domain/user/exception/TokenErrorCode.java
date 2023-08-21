package com.popple.server.domain.user.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum TokenErrorCode implements ErrorCode {
    INVALID_SIGNATURE_IN_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "해당 엑세스 토큰의 시그니처가 올바르지 않습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "해당 엑세스 토큰이 유효하지 않은 토큰입니다"),
    UNKNOWN_ACCESS_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "엑세스 토큰이 존재하지 않습니다."),
    WRONG_TYPE_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "해당 엑세스 토큰은 변조되었습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "해당 엑세스 토큰은 만료되었습니다."),

    INVALID_SIGNATURE_IN_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "해당 리프레시 토큰의 시그니처가 올바르지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "해당 리프레시 토큰이 유효하지 않은 토큰입니다. 다시 로그인 해주세요."),
    UNKNOWN_REFRESH_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 존재하지 않습니다."),
    WRONG_TYPE_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "해당 리프레시 토큰은 변조되었습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다. 보안을 위해 재로그인 해주세요"),
    NOT_EXIST_TOKEN(HttpStatus.BAD_REQUEST, "토큰은 필수 값입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    TokenErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}
