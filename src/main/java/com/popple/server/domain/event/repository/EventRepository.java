package com.popple.server.domain.event.repository;

import com.popple.server.domain.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAll(Pageable pageable);

    @Query("select m from Event m left join fetch m.host where m.id = :id")
    Optional<Event> findEventByIdJoinFetchSeller(@Param("id") Long id);

    @Modifying
    @Query("update Event e " +
            "set e.status = 'PROCEEDING' " +
            "where e.status != 'PROCEEDING' " +
            "and :now between substring(e.startDate, 1, 10) and substring(e.endDate, 1, 10) ")
    void updateProceedingEvent(@Param("now") String now);

    @Modifying
    @Query("update Event e " +
            "set e.status = 'WAIT' " +
            "where e.status != 'WAIT' " +
            "and :now < substring(e.startDate, 1, 10) ")
    void updateWaitEvent(@Param("now") String now);

    @Modifying
    @Query("update Event e " +
            "set e.status = 'END' " +
            "where e.status != 'END' " +
            "and :now > substring(e.endDate, 1, 10) ")
    void updateEndEvent(@Param("now") String now);
}