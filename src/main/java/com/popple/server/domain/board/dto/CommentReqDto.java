package com.popple.server.domain.board.dto;

import com.popple.server.domain.entity.Comment;
import com.popple.server.domain.entity.Member;
import com.popple.server.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentReqDto {
    @NotBlank(message = "댓글은 공백일 수 없습니다.")
    @Max(value = 200, message = "댓글은 최대 200자까지 입력가능합니다.")
    private String content;

    public Comment toEntity(Post post, Member member) {
        return Comment.builder()
                .member(member)
                .post(post)
                .content(this.content)
                .build();
    }
}

