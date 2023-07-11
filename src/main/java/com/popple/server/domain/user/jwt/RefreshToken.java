package com.popple.server.domain.user.jwt;

import com.popple.server.domain.user.vo.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refreshToken", timeToLive = 1209600)
@AllArgsConstructor
@Getter
@Builder
@ToString
public class RefreshToken {

    @Id
    private String refreshToken;
    private Long memberId;
    private Role role;
}
