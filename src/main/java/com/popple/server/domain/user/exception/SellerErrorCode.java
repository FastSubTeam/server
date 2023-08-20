package com.popple.server.domain.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SellerErrorCode implements ErrorCode {

    EXIST_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 셀러입니다"),
    INVALID_CHECK_DUPLICATION_PARAMETER(HttpStatus.BAD_REQUEST, "중복 확인을 하려는 파라미터를 입력해주세요"),
    NOT_FOUND_REGISTER_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 인증토큰입니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;
}
