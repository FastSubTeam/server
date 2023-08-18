package com.popple.server.domain.event.dto;

import com.popple.server.domain.entity.Event;
import com.popple.server.domain.entity.Seller;
import com.popple.server.domain.event.EventApproval;
import com.popple.server.domain.event.EventStatus;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
public class EventCreateReqDto {
    private String name;
    private String description;
    private String location;
    private String category;
    private String thumbnailUrl;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Event toEntity(Seller seller) {
        return Event.builder()
                .name(this.name)
                .description(this.description)
                .location(this.location)
                .category(this.category)
                .thumbnailUrl(this.thumbnailUrl)
                .startDate(toTimestamp(this.startDate))
                .endDate(toTimestamp(this.endDate))
                .approval(EventApproval.WAIT)
                .status(getCurrentStatus())
                .host(seller)
                .build();
    }

    private Timestamp toTimestamp(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }

    private EventStatus getCurrentStatus() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(this.endDate);
        System.out.println(now);
        if (this.endDate.isBefore(now)) {
            return EventStatus.END;
        }

        if (this.endDate.isAfter(now) && this.startDate.isBefore(now)) {
            return EventStatus.PROCEEDING;
        }

        return EventStatus.WAIT;
    }
}
