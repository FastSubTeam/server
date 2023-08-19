package com.popple.server.domain.event.dto;

import com.popple.server.domain.entity.Event;
import com.popple.server.domain.entity.Seller;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EventDetailRespDto {
    private Long id;
    private String thumbnailUrl;
    private String name;
    private String hostName;
    private String location;
    private String description;
    private String category;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;

    public static EventDetailRespDto fromEntity(Event event) {
        return EventDetailRespDto.builder()
                .id(event.getId())
                .thumbnailUrl(event.getThumbnailUrl())
                .name(event.getName())
                // .hostname(event.getHost().getNickname()) // TODO: 토큰 완료시 활성화
                .hostName("토큰 없는 익명 판매자")
                .location(event.getLocation())
                .description(event.getDescription())
                .category(event.getCategory())
                .startDate(event.getStartDate().toLocalDateTime())
                .endDate(event.getEndDate().toLocalDateTime())
                .createdAt(event.getCreatedAt().toLocalDateTime())
                .updatedAt(event.getUpdatedAt().toLocalDateTime())
                .status(event.getStatus().getValue())
                .build();
    }
}
