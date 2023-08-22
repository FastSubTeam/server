package com.popple.server.domain.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(
        name = "seller_event",
        uniqueConstraints =
        @UniqueConstraint(
                name = "SELLER_EVENT_UNIQUE_IDX",
                columnNames = {"seller_id", "event_id"}
        )
)
public class SellerEvent extends BaseEntity {

    public SellerEvent(Seller seller, Event event) {
        this.seller = seller;
        this.event = event;
        this.approval = "APPROVAL"; // TODO: 승인, 반려 기능은 아직 넣을지 말지 확정 X
    }

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
