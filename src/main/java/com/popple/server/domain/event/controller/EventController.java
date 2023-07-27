package com.popple.server.domain.event.controller;

import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.event.EventApproval;
import com.popple.server.domain.event.dto.EventCreateReqDto;
import com.popple.server.domain.event.dto.EventRespDto;
import com.popple.server.domain.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.awt.*;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EventController {

    private final EventService eventService;

    @PostMapping("/events")
    public APIDataResponse<EventRespDto> createEvent(@RequestBody EventCreateReqDto dto){
        EventRespDto eventRespDto = eventService.save(dto);
        return APIDataResponse.of(HttpStatus.CREATED, eventRespDto);
    }


    @GetMapping("/events")
    public APIDataResponse<List<EventRespDto>> EventList() {
        List<EventRespDto> events = eventService.findAll();

        return APIDataResponse.of(HttpStatus.OK, events);
    }

//    @GetMapping("/events")                             //한페이지당 최대게시글수 12
//    public APIDataResponse<Page<EventRespDto>> EventList(@PageableDefault(size = 12) Pageable pageable) {
//        Page<EventRespDto> eventPage = eventService.findAllByPage(pageable);
//
//        return APIDataResponse.of(HttpStatus.OK, eventPage);
//    }

    @GetMapping("/events/{id}")
    public APIDataResponse<EventRespDto> eventById(@PathVariable Long id) {
        EventRespDto eventRespDto = eventService.findById(id);

        return APIDataResponse.of(HttpStatus.OK, eventRespDto);
    }








}
