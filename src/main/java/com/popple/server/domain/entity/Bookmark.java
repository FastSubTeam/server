package com.popple.server.domain.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    private Timestamp createdAt;
}
