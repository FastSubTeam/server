package com.popple.server.domain.survey.controller;

import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.survey.dto.SurveyResultCreateReqDto;
import com.popple.server.domain.survey.service.SurveyService;
import com.popple.server.domain.user.annotation.LoginActor;
import com.popple.server.domain.user.vo.Actor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/survey")
public class SurveyController {

    private final SurveyService surveyService;

    @GetMapping("/active")
    public APIDataResponse<?> getActiveSurvey(@LoginActor Actor loginMember) {
        return this.surveyService.findActiveSurvey(loginMember);
    }

    @GetMapping("/result")
    public APIDataResponse<?> getSurveyResults() {
        return this.surveyService.getSurveyResults();
    }

    @PostMapping
    public APIDataResponse<?> submitSurvey(
            @RequestBody SurveyResultCreateReqDto dto, @LoginActor Actor loginMember) {
        return this.surveyService.createSurveyResult(dto, loginMember);
    }
}
