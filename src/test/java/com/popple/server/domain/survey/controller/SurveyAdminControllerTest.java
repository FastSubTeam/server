package com.popple.server.domain.survey.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.popple.server.domain.survey.dto.OptionCreateDto;
import com.popple.server.domain.survey.dto.SurveyCreateReqDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class SurveyAdminControllerTest {

    @Autowired
    private MockMvc mvc;

    private final ObjectMapper om = new ObjectMapper();

    @Test
    @DisplayName("[POST] 관리자 수요조사 등록 성공")
    void createSurveySuccess() throws Exception {
        // Given
        String title = "12월에 받고 싶은 선물은?";
        Timestamp startDate = Timestamp.valueOf(LocalDateTime.now().plusDays(1L));
        Timestamp endDate = Timestamp.valueOf(LocalDateTime.now().plusMonths(1L));
        List<OptionCreateDto> options = List.of(
                new OptionCreateDto("산타인형"),
                new OptionCreateDto("닌텐도"),
                new OptionCreateDto("트리")
        );

        // When
        ResultActions resultActions = requestCreateSurvey(title, startDate, endDate, options);

        // Then
        resultActions.andExpect(jsonPath("$.statusCode", HttpStatus.CREATED.value()).exists());
    }

    @Test
    @DisplayName("[POST] 관리자 수요조사 등록 실패 - 오늘보다 과거 날짜 선택")
    void createSurveyFailWithPastThanToday() throws Exception {
        // Given
        String title = "12월에 받고 싶은 선물은?";
        Timestamp startDate = Timestamp.valueOf(LocalDateTime.now().minusDays(2L));
        Timestamp endDate = Timestamp.valueOf(LocalDateTime.now().minusDays(1L));
        List<OptionCreateDto> options = List.of(
                new OptionCreateDto("산타인형"),
                new OptionCreateDto("닌텐도"),
                new OptionCreateDto("트리")
        );

        // When
        ResultActions resultActions = requestCreateSurvey(title, startDate, endDate, options);

        // Then
        String errorMessage = "유효성 검사 실패";
        resultActions.andExpect(jsonPath("$.errorCode", HttpStatus.BAD_REQUEST.value()).exists());
        resultActions.andExpect(jsonPath("$.message", errorMessage).exists());
    }

    @Test
    @DisplayName("[POST] 관리자 수요조사 등록 실패 - Title Blank")
    void createSurveyFailWithBlankTitle() throws Exception {
        // Given
        String title = "";
        Timestamp startDate = Timestamp.valueOf(LocalDateTime.now().plusDays(1L));
        Timestamp endDate = Timestamp.valueOf(LocalDateTime.now().plusMonths(1L));
        List<OptionCreateDto> options = List.of(
                new OptionCreateDto("산타인형"),
                new OptionCreateDto("닌텐도"),
                new OptionCreateDto("트리")
        );

        // When
        ResultActions resultActions = requestCreateSurvey(title, startDate, endDate, options);

        // Then
        String errorMessage = "유효성 검사 실패";
        resultActions.andExpect(jsonPath("$.errorCode", HttpStatus.BAD_REQUEST.value()).exists());
        resultActions.andExpect(jsonPath("$.message", errorMessage).exists());
    }

    @Test
    @DisplayName("[POST] 관리자 수요조사 등록 실패 - option 1개 선택")
    void createSurveyFailWithMinOptionsSize() throws Exception {
        // Given
        String title = "12월에 받고 싶은 선물은?";
        Timestamp startDate = Timestamp.valueOf(LocalDateTime.now().plusDays(1L));
        Timestamp endDate = Timestamp.valueOf(LocalDateTime.now().plusMonths(1L));
        List<OptionCreateDto> options = List.of(
                new OptionCreateDto("산타인형")
        );

        // When
        ResultActions resultActions = requestCreateSurvey(title, startDate, endDate, options);

        // Then
        String errorMessage = "유효성 검사 실패";
        resultActions.andExpect(jsonPath("$.errorCode", HttpStatus.BAD_REQUEST.value()).exists());
        resultActions.andExpect(jsonPath("$.message", errorMessage).exists());
    }

    @Test
    @DisplayName("[POST] 관리자 수요조사 등록 실패 - endDate가 startDate와 같은 경우")
    void createSurveyFailWithEndDateEqualsStartDate() throws Exception {
        // Given
        String title = "12월에 받고 싶은 선물은?";
        Timestamp date = Timestamp.valueOf(LocalDateTime.now().plusDays(1L));
        List<OptionCreateDto> options = List.of(
                new OptionCreateDto("산타인형"),
                new OptionCreateDto("닌텐도"),
                new OptionCreateDto("트리")
        );

        // When
        ResultActions resultActions = requestCreateSurvey(title, date, date, options);

        // Then
        String errorMessage = "수요조사 종료 날짜가 시작 날짜와 같거나 과거일 수 없습니다.";
        resultActions.andExpect(jsonPath("$.errorCode", HttpStatus.BAD_REQUEST.value()).exists());
        resultActions.andExpect(jsonPath("$.message", errorMessage).exists());
    }

    @Test
    @DisplayName("[POST] 관리자 수요조사 등록 실패 - endDate가 startDate 보다 과거 일 경우")
    void createSurveyFailWithEndDateBeforeStartDate() throws Exception {
        // Given
        String title = "12월에 받고 싶은 선물은?";
        Timestamp startDate = Timestamp.valueOf(LocalDateTime.now().plusDays(3L));
        Timestamp endDate = Timestamp.valueOf(LocalDateTime.now().plusDays(2L));
        List<OptionCreateDto> options = List.of(
                new OptionCreateDto("산타인형"),
                new OptionCreateDto("닌텐도"),
                new OptionCreateDto("트리")
        );

        // When
        ResultActions resultActions = requestCreateSurvey(title, startDate, endDate, options);

        // Then
        String errorMessage = "수요조사 종료 날짜가 시작 날짜와 같거나 과거일 수 없습니다.";
        resultActions.andExpect(jsonPath("$.errorCode", HttpStatus.BAD_REQUEST.value()).exists());
        resultActions.andExpect(jsonPath("$.message", errorMessage).exists());
    }

    private ResultActions requestCreateSurvey(
            String title, Timestamp startDate, Timestamp endDate, List<OptionCreateDto> options) throws Exception {

        SurveyCreateReqDto dto = new SurveyCreateReqDto();
        dto.setTitle(title);
        dto.setStartDate(startDate);
        dto.setEndDate(endDate);
        dto.setOptions(options);

        String jsonData = om.writeValueAsString(dto);

        return mvc.perform(post("/api/admin/survey")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(jsonData));
    }
}