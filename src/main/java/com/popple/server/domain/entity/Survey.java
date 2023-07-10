package com.popple.server.domain.entity;

import com.popple.server.domain.survey.type.SurveyStatus;
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

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private Timestamp startDate;

    @Column(nullable = false)
    private Timestamp endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private SurveyStatus status;

    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;
}
