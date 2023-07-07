package com.popple.server.domain.user;

import com.popple.server.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateUserResponseDto {

    private Long id;
    private String email;
    private String nickname;
    private String city;
    private String district;


    public static CreateUserResponseDto from(User user) {
        return CreateUserResponseDto.builder()
                .city(user.getCity())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .district(user.getDistrict())
                .id(user.getId())
                .build();
    }
}
