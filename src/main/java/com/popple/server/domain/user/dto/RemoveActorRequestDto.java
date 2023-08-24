package com.popple.server.domain.user.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class RemoveActorRequestDto {

    @Email
    @NotNull
    @NotEmpty
    private String email;

    private String password;
}
