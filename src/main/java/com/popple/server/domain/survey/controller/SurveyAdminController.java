package com.popple.server.domain.survey.controller;

import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.entity.Survey;
import com.popple.server.domain.survey.dto.SurveyDetailRespDto;
import com.popple.server.domain.survey.exception.RequestInvalidException;
import com.popple.server.domain.survey.service.SurveyService;
import com.popple.server.domain.survey.dto.SurveyCreateReqDto;
import com.popple.server.domain.survey.dto.SurveyRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/survey")
public class SurveyAdminController {

    private final SurveyService surveyService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public APIDataResponse<SurveyRespDto> createSurvey(
            @Valid @RequestBody SurveyCreateReqDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RequestInvalidException("유효성 검사 실패", bindingResult.getAllErrors());
        }
        if (!checkEndDateAfterStartDate(dto)) {
            throw new RequestInvalidException("수요조사 종료 날짜가 시작 날짜와 같거나 과거일 수 없습니다.");
        }
        SurveyRespDto respDto = surveyService.save(dto);

        return APIDataResponse.of(HttpStatus.CREATED, respDto);
    }

    private boolean checkEndDateAfterStartDate(SurveyCreateReqDto dto) {
        return dto.getEndDate().after(dto.getStartDate());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public APIDataResponse<List<SurveyRespDto>> surveyList() {
        List<SurveyRespDto> surveys = surveyService.findAll();

        return APIDataResponse.of(HttpStatus.OK, surveys);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public APIDataResponse<SurveyDetailRespDto> surveyDetail(@PathVariable int id) {
        SurveyDetailRespDto surveyDetailRespDto = surveyService.findById(id);

        return APIDataResponse.of(HttpStatus.OK, surveyDetailRespDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public APIDataResponse<?> deleteSurvey(@PathVariable int id) {
        surveyService.deleteById(id);

        return APIDataResponse.of(HttpStatus.OK, null);
    }
}
