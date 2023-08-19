package com.popple.server.domain.entity;

import com.popple.server.domain.event.EventApproval;
import com.popple.server.domain.event.EventStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Seller host;

    @Column(nullable = false, length = 300)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 200 )
    private String location;

    @Column(nullable = false, length = 1000)
    private String thumbnailUrl;

    @Column(nullable = false)
    private Timestamp startDate;

    @Column(nullable = false)
    private Timestamp endDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private EventStatus status;

    @Column(length = 20, nullable = false)
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private EventApproval approval;

    @Column(nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private Timestamp updatedAt;
}
