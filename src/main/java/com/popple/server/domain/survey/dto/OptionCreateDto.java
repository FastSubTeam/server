package com.popple.server.domain.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OptionCreateDto {
    @NotBlank(message = "선택지의 내용은 비어있을 수 없습니다.")
    private String content;
}
