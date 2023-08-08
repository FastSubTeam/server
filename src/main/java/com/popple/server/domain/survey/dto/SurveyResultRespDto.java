package com.popple.server.domain.survey.dto;

import com.popple.server.domain.entity.Survey;
import com.popple.server.domain.survey.type.SurveyStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class SurveyResultRespDto {
    private Integer id;
    private String title;
    private SurveyStatus status;
    private LocalDate startDate;
    private LocalDate endDate;

    public static SurveyResultRespDto fromEntity(Survey entity) {
        return SurveyResultRespDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .status(entity.getStatus())
                .startDate(entity.getStartDate().toLocalDateTime().toLocalDate())
                .endDate(entity.getEndDate().toLocalDateTime().toLocalDate())
                .build();
    }
}
