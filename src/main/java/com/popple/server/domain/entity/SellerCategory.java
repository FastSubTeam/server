package com.popple.server.domain.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class SellerCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Seller seller;

    @ManyToOne
    private Category category;
    private Timestamp createdAt;
}
