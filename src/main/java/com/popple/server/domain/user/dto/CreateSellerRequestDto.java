package com.popple.server.domain.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateSellerRequestDto {

    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

    @Email
    @NotNull
    @NotEmpty
    private String email;

    @Pattern(regexp = PASSWORD_REGEX, message = "비밀번호는 최소8글자, 최소 숫자1개, 문자1개를 포함하며 8글자 이상이어야 합니다.")
    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private String address;

    @NotNull
    @NotEmpty
    private String nickname;

    @NotNull
    @NotEmpty
    private String shopName;

    @NotNull
    @NotEmpty
    private String businessNumber;
}