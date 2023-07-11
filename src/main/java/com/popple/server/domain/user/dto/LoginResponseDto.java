package com.popple.server.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {

    private Long id;
    private String email;
    private String profileImgUrl;
    private String accessToken;
    private String refreshToken;

}
