package com.popple.server.domain.event.exception;

public interface EventExceptionMessage {
    String NON_EXIST_EVENT = "존재 하지 않는 행사 정보입니다.";
    String NON_EXIST_SELLER = "존재하지 않는 개인 판매자 정보입니다.";
    String NONE_VALID_LOGIN_SELLER = "유저가 존재하지 않거나, 개인 판매자가 아닌 유저 정보입니다.";
}
