package com.popple.server.domain.entity;

import com.popple.server.domain.survey.type.SurveyStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Survey extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Column(nullable = false, length = 100)
    private String title;

    @Setter
    @Column(nullable = false)
    private LocalDate startDate;

    @Setter
    @Column(nullable = false)
    private LocalDate endDate;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SurveyStatus status;
}
