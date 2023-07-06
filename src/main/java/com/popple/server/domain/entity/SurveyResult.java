package com.popple.server.domain.entity;

import javax.persistence.*;

@Entity
public class SurveyResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @ManyToOne
    @JoinColumn(name = "survey_option_id")
    private SurveyOption surveyOption;

    private Integer age;
}
