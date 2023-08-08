package com.popple.server.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Getter
public class LoginRequestDto {

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}
