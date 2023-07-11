package com.popple.server.domain.user.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Actor {

    private Long id;
    private Role role;
}
