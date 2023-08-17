package com.popple.server.domain.survey.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class SurveyResultCreateReqDto {
    @NotNull
    private Integer surveyId;

    @NotNull
    private Integer surveyOptionId;

    @NotNull
    private Integer age;
}
