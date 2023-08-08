package com.popple.server.domain.user.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class VerifyEmailRequestDto {

    @NotNull
    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @NotNull
    private String registerToken;
}
