package com.popple.server.domain.event.controller;

import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.event.dto.EventCreateReqDto;
import com.popple.server.domain.event.dto.EventDetailRespDto;
import com.popple.server.domain.event.dto.EventRespDto;
import com.popple.server.domain.event.dto.EventUpdateReqDto;
import com.popple.server.domain.event.exception.EventException;
import com.popple.server.domain.event.service.EventService;
import com.popple.server.domain.user.annotation.LoginActor;
import com.popple.server.domain.user.vo.Actor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.popple.server.domain.event.exception.EventExceptionMessage.FAIL_VALIDATION_CHECK;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EventController {

    private final EventService eventService;

    @PostMapping("/events")
    public APIDataResponse<EventRespDto> createEvent(@RequestBody EventCreateReqDto dto, @LoginActor Actor loginSeller){
        eventService.save(dto, loginSeller);

        return APIDataResponse.of(HttpStatus.CREATED, null);
    }

    @PutMapping("/events/{id}")
    public APIDataResponse<?> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventUpdateReqDto dto,
            BindingResult bindingResult,
            @LoginActor Actor loginSeller
    ) {
        checkValidation(bindingResult);
        eventService.update(id, dto, loginSeller);

        return APIDataResponse.empty(HttpStatus.OK);
    }

    @DeleteMapping("/events/{id}")
    public APIDataResponse<?> deleteEvent(@PathVariable Long id, @LoginActor Actor loginSeller) {
        eventService.delete(id, loginSeller);
        return APIDataResponse.empty(HttpStatus.OK);
    }

    private void checkValidation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new EventException(FAIL_VALIDATION_CHECK);
        }
    }

    @GetMapping("/events")
    public APIDataResponse<Page<EventRespDto>> EventList(@PageableDefault(size = 12) Pageable pageable) {
        Page<EventRespDto> eventPage = eventService.findAllByPage(pageable);

        return APIDataResponse.of(HttpStatus.OK, eventPage);
    }

    @GetMapping("/events/{id}")
    public APIDataResponse<EventDetailRespDto> eventById(@PathVariable Long id, @LoginActor Actor loginActor) {
        EventDetailRespDto eventDetailDto = eventService.findEventDetail(id, loginActor);

        return APIDataResponse.of(HttpStatus.OK, eventDetailDto);
    }

    @PostMapping("/events/join/{id}")
    public APIDataResponse<?> joinEvent(@PathVariable Long id, @LoginActor Actor loginSeller) {
        eventService.joinEvent(id, loginSeller);

        return APIDataResponse.empty(HttpStatus.OK);
    }

    @DeleteMapping("/events/join/{id}")
    public APIDataResponse<?> cancelJoinEvent(@PathVariable Long id, @LoginActor Actor loginSeller) {
        eventService.cancelJoinEvent(id, loginSeller);

        return APIDataResponse.empty(HttpStatus.OK);
    }
}
