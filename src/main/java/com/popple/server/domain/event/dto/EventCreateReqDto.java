package com.popple.server.domain.event.dto;

import com.popple.server.domain.entity.Event;
import com.popple.server.domain.entity.Seller;
import com.popple.server.domain.event.EventStatus;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
public class EventCreateReqDto {
    private String name;
    private String description;
    private String city;
    private String district;
    private String category;
    private String thumbnailUrl;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Event toEntity(Seller seller) {
        return Event.builder()
                .name(name)
                .description(description)
                .city(city)
                .district(district)
                .category(category)
                .thumbnailUrl(thumbnailUrl)
                .startDate(startDate)
                .endDate(endDate)
                .status(getCurrentStatus())
                .host(seller)
                .build();
    }

    private EventStatus getCurrentStatus() {
        LocalDateTime now = LocalDateTime.now();
        if (endDate.isBefore(now)) {
            return EventStatus.END;
        }

        if (endDate.isAfter(now) && startDate.isBefore(now)) {
            return EventStatus.PROCEEDING;
        }

        return EventStatus.WAIT;
    }
}
