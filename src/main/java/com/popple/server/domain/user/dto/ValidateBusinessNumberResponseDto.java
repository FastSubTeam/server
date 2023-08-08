package com.popple.server.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidateBusinessNumberResponseDto {

    @JsonProperty("match_cnt")
    private Integer matchCount;
}