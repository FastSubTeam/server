package com.popple.server.domain.user.dto;

import lombok.*;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ValidateBusinessNumberRequestDto {

    @Size(min = 1, max = 10)
    private List<String> b_no;
}