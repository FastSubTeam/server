package com.popple.server.domain.event.dto;

import com.popple.server.domain.entity.Event;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class EventDetailRespDto {
    private Long id;
    private String thumbnailUrl;
    private String name;
    private String nickname;
    private String city;
    private String district;
    private String description;
    private String category;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;
    private Boolean isOwner;
    private Boolean isParticipant;
    private List<EventParticipantRepDto> participants;

    public static EventDetailRespDto fromEntity(
            Event event, Boolean isOwner, Boolean isParticipant, List<EventParticipantRepDto> participants) {
        return EventDetailRespDto.builder()
                .id(event.getId())
                .thumbnailUrl(event.getThumbnailUrl())
                .name(event.getName())
                .nickname(event.getHost().getNickname())
                .city(event.getCity())
                .district(event.getDistrict())
                .description(event.getDescription())
                .category(event.getCategory())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .status(event.getStatus().getValue())
                .isOwner(isOwner)
                .isParticipant(isParticipant)
                .participants(participants)
                .build();
    }
}
