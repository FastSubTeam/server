package com.popple.server.domain.board.dto;

import com.popple.server.domain.entity.Comment;
import com.popple.server.domain.entity.Member;
import lombok.Getter;

import java.sql.Timestamp;

public interface CommentTableProjection {
    Long getId();
    Long getMemberId();
    String getContent();
    Timestamp getCreatedAt();
    Timestamp getUpdatedAt();
}
