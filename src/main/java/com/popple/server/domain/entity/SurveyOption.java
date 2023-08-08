package com.popple.server.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class SurveyOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Survey survey;

    @Setter
    @Column(nullable = false, length = 300)
    private String content;

    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;
}
