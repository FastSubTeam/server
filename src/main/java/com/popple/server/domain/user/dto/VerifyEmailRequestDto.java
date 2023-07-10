package com.popple.server.domain.user.dto;

import lombok.Getter;

@Getter
public class VerifyEmailRequestDto {

    private String registerToken;
}
