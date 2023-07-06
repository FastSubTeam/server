package com.popple.server.domain.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String title;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
