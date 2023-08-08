package com.popple.server.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailSource {
    private String subject;
    private String to;
}
