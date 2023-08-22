package com.popple.server.domain.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class EventParticipantRepDto {
    private Long sellerId;
    private String nickname;
    private String profileImgUrl;
}
