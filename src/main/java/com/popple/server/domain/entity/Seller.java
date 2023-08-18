package com.popple.server.domain.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Getter
@Entity
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String address;
    private String nickname;
    private String shopName;
    private String bio;
    private String profileImgUrl;
    private String businessNumber;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
