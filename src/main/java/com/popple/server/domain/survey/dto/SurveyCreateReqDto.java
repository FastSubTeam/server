package com.popple.server.domain.survey.dto;

import lombok.*;

import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyCreateReqDto {
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
    private List<OptionCreateDto> options;

}
