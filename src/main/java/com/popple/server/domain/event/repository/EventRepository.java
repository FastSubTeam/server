package com.popple.server.domain.event.repository;

import com.popple.server.domain.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAll(Pageable pageable);

    @Query("select m from Event m left join fetch m.host where m.id = :id")
    Optional<Event> findEventByIdJoinFetchSeller(@Param("id") Long id);
}