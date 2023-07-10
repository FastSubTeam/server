package com.popple.server.domain.survey.controller;

import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.entity.Survey;
import com.popple.server.domain.survey.service.SurveyService;
import com.popple.server.domain.survey.dto.SurveyCreateReqDto;
import com.popple.server.domain.survey.dto.SurveyRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/survey")
public class SurveyAdminController {

    private final SurveyService surveyService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public APIDataResponse<SurveyRespDto> createSurvey(
            @Valid @RequestBody SurveyCreateReqDto dto, BindingResult bindingResult) {
        Survey survey = surveyService.save(dto);
        SurveyRespDto respDto = SurveyRespDto.fromEntity(survey);

        return APIDataResponse.of(HttpStatus.CREATED, respDto);
    }
}
