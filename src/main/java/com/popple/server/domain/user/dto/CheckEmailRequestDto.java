package com.popple.server.domain.user.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
public class CheckEmailRequestDto {

    @Email
    @NotNull
    private String email;
}
