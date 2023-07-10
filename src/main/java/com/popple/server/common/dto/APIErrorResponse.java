package com.popple.server.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class APIErrorResponse {

    private final Integer errorCode;
    private final String message;

    public static APIErrorResponse of(Integer errorCode, String message) {
        return new APIErrorResponse(errorCode, message);
    }
}