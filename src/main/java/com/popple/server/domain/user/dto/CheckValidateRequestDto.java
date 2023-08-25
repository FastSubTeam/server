package com.popple.server.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class CheckValidateRequestDto {
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
    private static final String NICKNAME_REGEX = "^[a-z0-9._-]{2,16}$";


    @Email
    private String email;

    @Pattern(regexp = NICKNAME_REGEX, message = "닉네임은 영어, 숫자, 특수문자('.', '_', '-')로 구성되며 2글자 이상 10글자 이하입니다.")
    private String nickname;
}
