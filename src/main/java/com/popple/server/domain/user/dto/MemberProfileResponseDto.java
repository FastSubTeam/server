package com.popple.server.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberProfileResponseDto {

    private String nickname;
    private String profileImgUrl;
}
