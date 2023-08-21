package com.popple.server.domain.event.dto;

import com.popple.server.domain.entity.Event;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventRespDto {

    private Long id;
    private String name;
    private String city;
    private String district;
    private String thumbnailUrl;
    private String status;
    private Boolean bookmark;

    public static EventRespDto fromEntity(Event event){
        return EventRespDto.builder()
                .id(event.getId())
                .name(event.getName())
                .city(event.getCity())
                .district(event.getDistrict())
                .thumbnailUrl(event.getThumbnailUrl())
                .status(event.getStatus().getValue())
                .bookmark(false) // TODO: 북마크 기능 구현 전 일단 false
                .build();
    }

}
