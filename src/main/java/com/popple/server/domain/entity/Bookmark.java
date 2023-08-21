package com.popple.server.domain.entity;

import javax.persistence.*;

@Entity
@Table(
        name = "bookmark",
        uniqueConstraints =
        @UniqueConstraint(
                name = "MEMBER_BOOKMARK_UNIQUE_IDX",
                columnNames = {"member_id", "event_id"}
        )
)
public class Bookmark extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Event event;
}
