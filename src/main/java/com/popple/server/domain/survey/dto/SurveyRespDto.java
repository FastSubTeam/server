package com.popple.server.domain.survey.dto;

import com.popple.server.domain.entity.Survey;
import com.popple.server.domain.survey.type.SurveyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Builder
@Getter
public class SurveyRespDto {
    private Integer id;
    private String title;
    private Timestamp startDate;
    private Timestamp endDate;
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
