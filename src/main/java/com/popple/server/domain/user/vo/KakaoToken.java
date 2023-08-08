package com.popple.server.domain.user.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoToken {

    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private int expiresIn;
    private String scope;
    private int refreshTokenExpiresIn;

}