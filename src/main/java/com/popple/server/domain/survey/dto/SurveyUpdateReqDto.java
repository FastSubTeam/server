package com.popple.server.domain.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SurveyUpdateReqDto {
    private Integer id;

    @NotBlank(message = "수요조사 제목이 빈 값입니다.")
    private String title;

    @Future(message = "수요조사 시작일은 현재 시간보다 과거일 수 없습니다.")
    @NotNull(message = "수요조사 시작일은 필수 선택 값입니다.")
    private Timestamp startDate;

    @Future(message = "설문조사 종료일은 현재 시간보다 과거일 수 없습니다.")
    @NotNull(message = "수요조사 종료일은 필수 선택 값입니다.")
    private Timestamp endDate;

    @NotNull(message = "수요조사의 선택지는 반드시 존재해야 합니다.")
    @Size(min = 2, message = "수요조사의 선택지는 최소 2개 이상 선택해야 합니다.")
    private List<OptionUpdateReqDto> options;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class OptionUpdateReqDto {
        private Integer id;

        @NotBlank(message = "선택지의 내용은 비어있을 수 없습니다.")
        private String content;
    }
}
