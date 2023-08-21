package com.popple.server.domain.user.dto;

import com.popple.server.domain.entity.Member;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateUserRequestDto {

    private static final String NICKNAME_REGEX = "^[a-z0-9._-]{2,10}$";
    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String city;

    @NotNull
    private String district;

    @NotNull
    @Pattern(regexp = NICKNAME_REGEX, message = "닉네임은 영어, 숫자, 특수문자('.', '_', '-')로 구성되며 2글자 이상 10글자 이하입니다.")
    private String nickname;

    public Member toEntity() {
        return Member.builder()
                .nickname(nickname)
                .city(city)
                .district(district)
                .email(email)
                .password(password)
                .build();
    }

}
