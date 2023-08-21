package com.popple.server.domain.entity;

import javax.persistence.*;

@Entity
@Table(
        name = "seller_event",
        uniqueConstraints =
        @UniqueConstraint(
                name = "SELLER_EVENT_UNIQUE_IDX",
                columnNames = {"seller_id", "event_id"}
        )
)
public class SellerEvent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Seller seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Event event;
    private String approval; // TODO: Enum으로 교체
}
