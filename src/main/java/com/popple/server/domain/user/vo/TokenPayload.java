package com.popple.server.domain.user.vo;


import com.popple.server.domain.entity.LoginPlatform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenPayload {

    private Long id;
    private Role role;
    private LoginPlatform platform;
}
