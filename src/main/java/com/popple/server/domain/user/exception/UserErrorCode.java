package com.popple.server.domain.user.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserErrorCode implements ErrorCode {

    EXIST_EMAIL(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다."),
    EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),

    PROCEEDING_EMAIL(HttpStatus.BAD_REQUEST, "이미 회원가입을 진행중인 이메일입니다"),
    NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다"),
    INVALID_CHECK_DUPLICATION_PARAMETER(HttpStatus.BAD_REQUEST, "중복 확인을 하려는 파라미터를 입력해주세요"),
    NOT_FOUND_REGISTER_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 인증토큰입니다."),
    NOT_PROCEEDING_REGISTERED_EMAIL(HttpStatus.BAD_REQUEST, "아직 인증이 완료되지 않은 이메일입니다."),
    INVALID_ADDRESS(HttpStatus.BAD_REQUEST, "올바르지 않은 주소입니다."),
    SEND_MAIL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송 과정에서 오류가 발생하였습니다."),
    INVALID_BUSINESS_NUMBER(HttpStatus.BAD_REQUEST, "올바르지 않은 사업자 번호입니다"),
    INVALID_LOGIN_PAYLOAD(HttpStatus.BAD_REQUEST, "로그인 정보가 올바르지 않습니다."),
    NEED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "엑세스 토큰을 재발급 받으려면 리프레시 토큰이 필요합니다"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "해당 API에 대한 권한이 존재하지 않습니다"),
    EXIST_BUSINESS_NUMBER(HttpStatus.BAD_REQUEST, "이미 존재하는 사업자 등록 번호입니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;

    UserErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}
