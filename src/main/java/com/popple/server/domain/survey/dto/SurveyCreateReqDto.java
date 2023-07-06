package com.popple.server.domain.survey.dto;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyCreateReqDto {
    private String title;
    private Timestamp startDate;
    private Timestamp endDate;
    private List<OptionCreateDto> options;

}
