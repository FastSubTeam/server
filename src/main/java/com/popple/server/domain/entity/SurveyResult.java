package com.popple.server.domain.entity;

import javax.persistence.*;

@Entity
public class SurveyResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Survey survey;

    @ManyToOne
    private SurveyOption surveyOption;

    private Integer age;
}
