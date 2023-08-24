package com.popple.server.domain.user.dto;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class CheckDuplicatedBusinessNumberRequestDto {

    @NotNull
    @NotEmpty
    private String businessNumber;
}
