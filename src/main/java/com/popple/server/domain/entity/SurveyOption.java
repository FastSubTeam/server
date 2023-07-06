package com.popple.server.domain.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class SurveyOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    private String content;
    private Timestamp createdAt;
}
