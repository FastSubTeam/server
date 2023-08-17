package com.popple.server.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
@ToString
public class PostRespDto {
    private Long id;
    private String nickname;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<CommentDto> comments;
}
