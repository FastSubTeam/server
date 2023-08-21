package com.popple.server.domain.survey.dto;

import com.popple.server.domain.entity.Survey;
import com.popple.server.domain.survey.type.SurveyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class SurveyRespDto {
    private Integer id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private SurveyStatus status;

    public static SurveyRespDto fromEntity(Survey survey) {
        return SurveyRespDto.builder()
                .id(survey.getId())
                .title(survey.getTitle())
                .startDate(survey.getStartDate())
                .endDate(survey.getEndDate())
                .status(survey.getStatus())
                .build();
    }
}
