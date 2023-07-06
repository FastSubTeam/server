package com.popple.server.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String city;
    private String district;
    private String nickname;
    private String profileImgUrl;
    private Boolean inactive;
    private Timestamp createdAt;
}
