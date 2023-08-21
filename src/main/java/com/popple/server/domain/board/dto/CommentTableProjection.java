package com.popple.server.domain.board.dto;

import java.time.LocalDateTime;

public interface CommentTableProjection {
    Long getId();
    Long getMemberId();
    String getContent();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
}
