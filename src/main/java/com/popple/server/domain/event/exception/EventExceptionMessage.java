package com.popple.server.domain.event.exception;

public interface EventExceptionMessage {
    String NON_EXIST_EVENT = "존재 하지 않는 행사 정보입니다.";
    String NON_EXIST_SELLER = "존재하지 않는 개인 판매자 정보입니다.";
    String NONE_VALID_LOGIN_SELLER = "유저가 존재하지 않거나, 개인 판매자가 아닌 유저 정보입니다.";
    String NOT_MATCH_OWNER_OF_EVENT = "해당 행사 정보를 작성한 판매자가 아닙니다.";
    String FAIL_VALIDATION_CHECK = "유효성 검사에 실패했습니다.";
    String ALREADY_JOIN_EVENT = "이미 해당 행사에 참여하는 개인 판매자입니다.";
    String NOT_JOIN_EVENT = "해당 행사에 참여하고 있지 않은 개인 판매자입니다.";
}
