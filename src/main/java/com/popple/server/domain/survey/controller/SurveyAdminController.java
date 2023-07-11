package com.popple.server.domain.survey.controller;

import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.survey.dto.SurveyCreateReqDto;
import com.popple.server.domain.survey.dto.SurveyDetailRespDto;
import com.popple.server.domain.survey.dto.SurveyRespDto;
import com.popple.server.domain.survey.dto.SurveyUpdateReqDto;
import com.popple.server.domain.survey.exception.RequestInvalidException;
import com.popple.server.domain.survey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
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
        if (!isValidEndDateAndStartDate(dto.getStartDate(), dto.getEndDate())) {
            throw new RequestInvalidException("수요조사 종료 날짜가 시작 날짜와 같거나 과거일 수 없습니다.");
        }
        SurveyRespDto respDto = surveyService.save(dto);

        return APIDataResponse.of(HttpStatus.CREATED, respDto);
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

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public APIDataResponse<?> updateSurvey(
            @Valid @RequestBody SurveyUpdateReqDto dto, BindingResult bindingResult, @PathVariable int id) {
        if (bindingResult.hasErrors() || dto.getId() != id) {
            throw new RequestInvalidException("유효성 검사 실패", bindingResult.getAllErrors());
        }
        if (!isValidEndDateAndStartDate(dto.getStartDate(), dto.getEndDate())) {
            throw new RequestInvalidException("수요조사 종료 날짜가 시작 날짜와 같거나 과거일 수 없습니다.");
        }
        surveyService.updateSurvey(dto, id);

        return APIDataResponse.of(HttpStatus.OK, null);
    }

    private boolean isValidEndDateAndStartDate(Timestamp startDate, Timestamp endDate) {
        return endDate.after(startDate);
    }
}
