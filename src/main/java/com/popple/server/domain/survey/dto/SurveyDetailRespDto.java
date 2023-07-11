package com.popple.server.domain.survey.dto;

import com.popple.server.domain.entity.Survey;
import com.popple.server.domain.entity.SurveyOption;
import com.popple.server.domain.survey.type.SurveyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SurveyDetailRespDto {
    private Integer id;
    private String title;
    private Timestamp startDate;
    private Timestamp endDate;
    private SurveyStatus status;
    private List<OptionRespDto> options;

    public static SurveyDetailRespDto fromEntity(Survey survey, List<SurveyOption> options) {
        List<OptionRespDto> convertedOptions = options.stream()
                .map(OptionRespDto::fromEntity).collect(Collectors.toList());

        return SurveyDetailRespDto.builder()
                .id(survey.getId())
                .title(survey.getTitle())
                .startDate(survey.getStartDate())
                .endDate(survey.getEndDate())
                .status(survey.getStatus())
                .options(convertedOptions)
                .build();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    static class OptionRespDto {
        private Integer id;
        private String content;

        public static OptionRespDto fromEntity(SurveyOption option) {
            return OptionRespDto.builder()
                    .id(option.getId())
                    .content(option.getContent())
                    .build();
        }
    }
}
