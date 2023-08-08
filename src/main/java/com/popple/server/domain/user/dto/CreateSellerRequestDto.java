package com.popple.server.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
@Builder
public class CreateSellerRequestDto {

    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

    @Email
    @NotNull
    private String email;

    @Pattern(regexp = PASSWORD_REGEX, message = "비밀번호는 최소8글자, 최소 숫자1개, 문자1개를 포함하며 8글자 이상이어야 합니다.")
    @NotNull
    private String password;

    @NotNull
    private String address;

    @NotNull
    private String nickname;

    @NotNull
    private String shopName;

    @NotNull
    private String businessNumber;

    @NotNull
    private List<Integer> categories;
}