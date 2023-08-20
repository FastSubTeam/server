package com.popple.server.domain.user.dto;

import com.popple.server.domain.user.vo.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {

    private Long userId;
    private Role role;
    private String email;
    private String nickname;
    private String profileImgUrl;
    private String accessToken;
    private String refreshToken;

}
