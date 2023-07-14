package com.popple.server.domain.event.controller;

import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.event.dto.EventCreateReqDto;
import com.popple.server.domain.event.dto.EventRespDto;
import com.popple.server.domain.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EventController {

    private final EventService eventService;

    @PostMapping("/events")
    public APIDataResponse<EventRespDto> createEvent(EventCreateReqDto dto){
        EventRespDto eventRespDto = eventService.save(dto);
        return APIDataResponse.of(HttpStatus.CREATED, eventRespDto);
    }






}
