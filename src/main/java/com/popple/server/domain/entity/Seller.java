package com.popple.server.domain.entity;

import com.popple.server.domain.user.vo.Role;
import com.popple.server.domain.user.vo.TokenPayload;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@DynamicInsert
public class Seller extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String shopName;

    @Column(nullable = false)
    @ColumnDefault("'Default Bio'")
    private String bio;

    @Column(nullable = false)
    @ColumnDefault("'DefaultImage'")
    private String profileImgUrl;

    @Column(nullable = false, unique = true)
    private String businessNumber;

    public TokenPayload toPayload() {
        return TokenPayload.builder()
                .id(id)
                .role(Role.ROLE_SELLER)
                .build();
    }
}
