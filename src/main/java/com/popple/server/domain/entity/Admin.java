package com.popple.server.domain.entity;

import com.popple.server.domain.user.vo.Role;
import com.popple.server.domain.user.vo.TokenPayload;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
public class Admin extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String password;

    public TokenPayload toPayload() {
        return TokenPayload.builder()
                .id(Long.valueOf(id))
                .role(Role.ROLE_ADMIN)
                .build();
    }
}