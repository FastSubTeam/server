package com.popple.server.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class KakaoLoginRequestDto {

    private String email;
    private String nickname;
}
