package com.popple.server.common.dto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class APIErrorResponse {

    private final Integer errorCode;
    private final String message;

    public static APIErrorResponse of(Integer errorCode, String message) {
        return new APIErrorResponse(errorCode, message);
    }
}