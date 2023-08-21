package com.popple.server.domain.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class EventUpdateReqDto {
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String city;

    @NotBlank
    private String district;

    @NotBlank
    private String thumbnailUrl;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;
}
