package com.popple.server.domain.entity;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String city;

    @NotNull
    private String district;

    @NotNull
    @Column(unique = true)
    private String nickname;


    //TODO 이후 디폴트 이미지를 정하고, 해당 이미지의 주소로 디폴트 값 변경 필요
    @ColumnDefault(value = "'profileDefaultImageUrl'")
    @NotNull
    private String profileImgUrl;

    @ColumnDefault(value = "false")
    @NotNull
    private Boolean inactive;

    @CreatedDate
    @NotNull
    private Timestamp createdAt;
}
