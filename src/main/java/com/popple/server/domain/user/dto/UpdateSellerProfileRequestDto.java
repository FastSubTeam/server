package com.popple.server.domain.user.dto;

import lombok.Getter;

@Getter
public class UpdateSellerProfileRequestDto {
    private String nickname;
    private String shopName;
    private String bio;
    private String profileImgUrl;
    private String address;
}
