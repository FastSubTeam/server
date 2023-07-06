package com.popple.server.domain.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "host_id")
    private Seller host;
    private String name;
    private String description;
    private String location;
    private String thumbnailUrl;
    private Timestamp startDate;
    private Timestamp endDate;
    private String approval; // TODO: Enum으로 교체
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
