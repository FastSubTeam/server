package com.popple.server.domain.event.repository;

import com.popple.server.domain.entity.Event;
import com.popple.server.domain.entity.Seller;
import com.popple.server.domain.entity.SellerEvent;
import com.popple.server.domain.event.dto.EventParticipantRepDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SellerEventRepository extends JpaRepository<SellerEvent, Long> {
    void deleteAllByEventId(Long eventId);
    boolean existsByEventAndSeller(Event event, Seller seller);
    void deleteByEventAndSeller(Event event, Seller seller);

    @Query("select new com.popple.server.domain.event.dto.EventParticipantRepDto(" +
            "   se.seller.id, " +
            "   se.seller.nickname, " +
            "   se.seller.profileImgUrl" +
            ") " +
            "from SellerEvent se " +
            "where se.event = :event")
    List<EventParticipantRepDto> findParticipantsByEvent(@Param("event") Event event);
}
