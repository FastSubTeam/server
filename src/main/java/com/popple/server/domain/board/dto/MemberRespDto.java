package com.popple.server.domain.board.dto;

import com.popple.server.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@Builder
public class MemberRespDto {
    private Long id;
    private String email;
    private String nickname;
    private String profileUrl;
    private Boolean inactive;

    public static MemberRespDto of(Member member){
        return MemberRespDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileUrl(member.getProfileImgUrl())
                .inactive(member.getInactive())
                .build();
    }
}
