package com.popple.server.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
@Setter
public class APIErrorResponse {

    private final Integer errorCode;
    private final String message;

    public static APIErrorResponse of(HttpStatus httpStatus, String message) {
        return new APIErrorResponse(httpStatus.value(), message);
    }
}