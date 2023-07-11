package com.popple.server.domain.user.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AccessTokenPayload {

    private Long userId;
    private String email;
    private Role role;
}
