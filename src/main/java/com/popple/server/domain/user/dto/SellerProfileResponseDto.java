package com.popple.server.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SellerProfileResponseDto {

    private String nickname;
    private String profileImgUrl;
    private String shopName;
    private String address;
    private String bio;
}
