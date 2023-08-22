package com.popple.server.domain.event.repository;

import com.popple.server.domain.entity.Event;
import com.popple.server.domain.entity.Seller;
import com.popple.server.domain.entity.SellerEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerEventRepository extends JpaRepository<SellerEvent, Long> {
    void deleteAllByEventId(Long eventId);
    boolean existsByEventAndSeller(Event event, Seller seller);
}
