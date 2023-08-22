package com.popple.server.domain.board.dto;

import com.popple.server.domain.entity.Comment;
import com.popple.server.domain.entity.Member;
import com.popple.server.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentReqDto {
    private String content;

    public Comment toEntity(Post post, Member member) {
        return Comment.builder()
                .member(member)
                .post(post)
                .content(this.content)
                .build();
    }
}

