package com.popple.server.domain.user.dto;

import com.popple.server.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateUserResponseDto {

    private Long id;
    private String email;
    private String nickname;
    private String city;
    private String district;


    public static CreateUserResponseDto from(Member member) {
        return CreateUserResponseDto.builder()
                .city(member.getCity())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .district(member.getDistrict())
                .id(member.getId())
                .build();
    }
}
