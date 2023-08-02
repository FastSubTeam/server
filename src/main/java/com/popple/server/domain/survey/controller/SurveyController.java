package com.popple.server.domain.survey.controller;

import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.survey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/survey")
public class SurveyController {

    private final SurveyService surveyService;

    @GetMapping("/active")
    public APIDataResponse<?> getActiveSurvey(Authentication authentication) {
        return this.surveyService.findActiveSurvey(authentication);
    }

    @GetMapping("/result")
    public APIDataResponse<?> getSurveyResults() {
        return surveyService.getSurveyResults();
    }
}
