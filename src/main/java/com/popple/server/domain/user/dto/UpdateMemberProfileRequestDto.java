package com.popple.server.domain.user.dto;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class UpdateMemberProfileRequestDto {
    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[a-z0-9._-]{2,10}$", message = "닉네임은 영문자, 숫자, '.', '_', '-'를 사용한 2글자이상 10글자 이하만 허용됩니다.")
    private String nickname;

    @NotNull
    @NotEmpty
    private String profileImgUrl;

    @NotNull
    @NotEmpty
    private String city;

    @NotNull
    @NotEmpty
    private String district;
}
