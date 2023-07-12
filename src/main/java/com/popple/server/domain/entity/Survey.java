package com.popple.server.domain.entity;

import com.popple.server.domain.survey.type.SurveyStatus;
import lombok.*;

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

    @Setter
    @Column(nullable = false, length = 100)
    private String title;

    @Setter
    @Column(nullable = false)
    private Timestamp startDate;

    @Setter
    @Column(nullable = false)
    private Timestamp endDate;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private SurveyStatus status;

    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;
}
