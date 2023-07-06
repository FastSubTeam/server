package com.popple.server.domain.survey;

import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.entity.Survey;
import com.popple.server.domain.survey.dto.SurveyCreateReqDto;
import com.popple.server.domain.survey.dto.SurveyRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/survey")
public class SurveyAdminController {

    private final SurveyService surveyService;

    // TODO: 예외처리
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public APIDataResponse<SurveyRespDto> createSurvey(@RequestBody SurveyCreateReqDto dto) {
        Survey survey = surveyService.save(dto);
        SurveyRespDto respDto = SurveyRespDto.fromEntity(survey);

        return APIDataResponse.of(HttpStatus.CREATED.value(), respDto);
    }
}
