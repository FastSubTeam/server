package com.popple.server.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class APIDataResponse<T> {
    private final Integer statusCode;
    private final T data;

    public static <T> APIDataResponse<T> of(HttpStatus httpStatus, T data) {
        return new APIDataResponse<>(httpStatus.value(), data);
    }

    public static <T> APIDataResponse<T> empty(HttpStatus httpStatus) {
        return new APIDataResponse<>(httpStatus.value(), null);
    }
}