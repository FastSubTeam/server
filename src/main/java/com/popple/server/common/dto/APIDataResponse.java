package com.popple.server.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIDataResponse<T> {
    private final Integer statusCode;
    private final T data;

    public static <T> APIDataResponse<T> of(Integer statusCode, T data) {
        return new APIDataResponse<>(statusCode, data);
    }

    public static <T> APIDataResponse<T> empty(Integer statusCode) {
        return new APIDataResponse<>(statusCode, null);
    }
}