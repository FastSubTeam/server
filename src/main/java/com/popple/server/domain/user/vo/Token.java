package com.popple.server.domain.user.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class Token {

    private final String accessToken;
    private final String refreshToken;
}
