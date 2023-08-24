package com.popple.server.domain.user.dto;

import lombok.Builder;

@Builder
public class MemberProfileResponseDto {

    private String nickname;
    private String profileImgUrl;
}
