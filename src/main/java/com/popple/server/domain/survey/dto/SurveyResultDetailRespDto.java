package com.popple.server.domain.survey.dto;

import com.popple.server.domain.entity.Survey;
import com.popple.server.domain.entity.SurveyOption;
import com.popple.server.domain.survey.type.SurveyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class SurveyResultDetailRespDto {
    private Integer id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private SurveyStatus status;
    private List<OptionRespDto> options;
    private List<SurveyAnswer> answers;

    public static SurveyResultDetailRespDto fromEntity(
            Survey survey, List<SurveyOption> options, List<SurveyAnswer> answers) {
        List<OptionRespDto> convertedOptions = options.stream()
                .map(OptionRespDto::fromEntity).collect(Collectors.toList());

        return SurveyResultDetailRespDto.builder()
                .id(survey.getId())
                .title(survey.getTitle())
                .startDate(survey.getStartDate())
                .endDate(survey.getEndDate())
                .status(survey.getStatus())
                .options(convertedOptions)
                .answers(answers)
                .build();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
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

    public interface SurveyAnswer {
        Integer getAnswerId();
        Long getUserId();
        Integer getOptionId();
        Integer getAge();
        String getArea();
    }
}
