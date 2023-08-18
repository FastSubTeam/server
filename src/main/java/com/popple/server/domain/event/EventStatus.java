package com.popple.server.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventStatus {
    WAIT("시작전"), PROCEEDING("진행중"), END("마감");

    private final String value;
}
