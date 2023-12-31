package com.popple.server.domain.board.dto;

import com.popple.server.domain.entity.Member;
import com.popple.server.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostReqDto {
    @NotBlank(message = "제목은 공백일 수 없습니다.")
    @Size(max = 100, message = "제목은 최대 100자까지 가능합니다.")
    private String title;

    @NotBlank(message = "본문은 공백일 수 없습니다.")
    private String content;

    public Post toEntity(Member member){
        return Post.builder()
                .member(member)
                .title(this.title)
                .content(this.content)
                .build();
    }
}
