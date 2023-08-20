package com.popple.server.domain.user.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReissueAccessTokenRequestDto {

    private String accessToken;
}
