package com.popple.server.domain.event.service;

import com.popple.server.domain.entity.Event;
import com.popple.server.domain.event.dto.EventCreateReqDto;
import com.popple.server.domain.event.dto.EventRespDto;
import com.popple.server.domain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static com.popple.server.domain.event.EventApproval.WAIT;
@RequiredArgsConstructor
@Service
public class EventService {
    private final EventRepository eventRepository;

    @Transactional
    public EventRespDto save(EventCreateReqDto dto) {
        Event event = Event.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .location(dto.getLocation())
                .thumbnailUrl(dto.getThumbnailUrl())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .approval(WAIT)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        eventRepository.save(event);

        return EventRespDto.fromEntity(event);
    }


}
