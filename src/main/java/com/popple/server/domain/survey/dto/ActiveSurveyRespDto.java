package com.popple.server.domain.survey.dto;

import com.popple.server.domain.entity.Survey;
import com.popple.server.domain.entity.SurveyOption;
import com.popple.server.domain.survey.type.SurveyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ActiveSurveyRespDto {
    private Integer id;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isDone;
    private SurveyStatus status;
    private List<ActiveSurveyRespDto.OptionRespDto> options;

    public static ActiveSurveyRespDto fromEntity(Survey survey, List<SurveyOption> options, Boolean isDone) {
        List<ActiveSurveyRespDto.OptionRespDto> convertedOptions = options.stream()
                .map(ActiveSurveyRespDto.OptionRespDto::fromEntity).collect(Collectors.toList());

        return ActiveSurveyRespDto.builder()
                .id(survey.getId())
                .title(survey.getTitle())
                .startDate(survey.getStartDate())
                .endDate(survey.getEndDate())
                .isDone(isDone)
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

        public static ActiveSurveyRespDto.OptionRespDto fromEntity(SurveyOption option) {
            return ActiveSurveyRespDto.OptionRespDto.builder()
                    .id(option.getId())
                    .content(option.getContent())
                    .build();
        }
    }
}
