package com.popple.server.domain.event.controller;

import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.event.dto.EventCreateReqDto;
import com.popple.server.domain.event.dto.EventDetailRespDto;
import com.popple.server.domain.event.dto.EventRespDto;
import com.popple.server.domain.event.service.EventService;
import com.popple.server.domain.user.annotation.LoginActor;
import com.popple.server.domain.user.vo.Actor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EventController {

    private final EventService eventService;

    @PostMapping("/events")
    public APIDataResponse<EventRespDto> createEvent(@RequestBody EventCreateReqDto dto, @LoginActor Actor loginSeller){
        // TODO: 토큰에서 유저를 뽑아내야 한다. + 존재하는 셀러인지 확인
        eventService.save(dto, loginSeller);

        return APIDataResponse.of(HttpStatus.CREATED, null);
    }

    @GetMapping("/events")
    public APIDataResponse<Page<EventRespDto>> EventList(@PageableDefault(size = 12) Pageable pageable) {
        Page<EventRespDto> eventPage = eventService.findAllByPage(pageable);

        return APIDataResponse.of(HttpStatus.OK, eventPage);
    }

    @GetMapping("/events/{id}")
    public APIDataResponse<EventDetailRespDto> eventById(@PathVariable Long id) {
        EventDetailRespDto eventDetailDto = eventService.findEventDetail(id);

        return APIDataResponse.of(HttpStatus.OK, eventDetailDto);
    }








}
