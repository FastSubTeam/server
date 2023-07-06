package com.popple.server.domain.entity;

import com.popple.server.domain.survey.SurveyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private Timestamp startDate;
    private Timestamp endDate;

    @Enumerated(EnumType.STRING)
    private SurveyStatus status;
    private Timestamp createdAt;
}
