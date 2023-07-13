package com.popple.server.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.popple.server.common.dto.APIDataResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class BoardAPIDataResponse<T> extends APIDataResponse<T> {
    private Long totalPosts;

    public BoardAPIDataResponse(Integer statusCode, T data, Long totalPosts) {
        super(statusCode, data);
        this.totalPosts = totalPosts;
    }

    public static<T> BoardAPIDataResponse<T> of(HttpStatus httpStatus, T data, Long totalPosts){
        return new BoardAPIDataResponse<>(httpStatus.value(), data, totalPosts);
    }
}
