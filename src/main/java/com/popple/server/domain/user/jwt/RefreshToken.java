package com.popple.server.domain.user.jwt;

import com.popple.server.domain.user.vo.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refreshToken", timeToLive = 1209600)
@AllArgsConstructor
@Getter
@Builder
@ToString
@DynamicInsert
public class RefreshToken {

    @Id
    private String token;
    private Long memberId;
    private Role role;

    @ColumnDefault("'false'")
    private boolean isBlackList;
}
