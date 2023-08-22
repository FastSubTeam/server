package com.popple.server.domain.event.service;

import com.popple.server.domain.entity.Event;
import com.popple.server.domain.entity.Seller;
import com.popple.server.domain.entity.SellerEvent;
import com.popple.server.domain.event.EventStatus;
import com.popple.server.domain.event.dto.EventCreateReqDto;
import com.popple.server.domain.event.dto.EventDetailRespDto;
import com.popple.server.domain.event.dto.EventRespDto;
import com.popple.server.domain.event.dto.EventUpdateReqDto;
import com.popple.server.domain.event.exception.EventException;
import com.popple.server.domain.event.repository.EventRepository;
import com.popple.server.domain.event.repository.SellerEventRepository;
import com.popple.server.domain.user.repository.SellerRepository;
import com.popple.server.domain.user.vo.Actor;
import com.popple.server.domain.user.vo.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.popple.server.domain.event.exception.EventExceptionMessage.*;

@RequiredArgsConstructor
@Service
public class EventService {
    private final EventRepository eventRepository;
    private final SellerEventRepository sellerEventRepository;
    private final SellerRepository sellerRepository;

    @Transactional
    public void save(EventCreateReqDto dto, Actor loginSeller) {
        checkSeller(loginSeller);
        Seller seller = getSellerByLoginSeller(loginSeller);
        Event event = dto.toEntity(seller);
        eventRepository.save(event);

        SellerEvent sellerEvent = new SellerEvent(seller, event);
        sellerEventRepository.save(sellerEvent);
    }

    @Transactional
    public void update(Long id, EventUpdateReqDto dto, Actor loginSeller) {
        checkSeller(loginSeller);

        Event event = eventRepository.findEventByIdJoinFetchSeller(id)
                .orElseThrow(() -> new EventException(NON_EXIST_EVENT));
        checkEventOwner(event, loginSeller.getId());
        updateEvent(event, dto);
    }

    @Transactional
    public void delete(Long id, Actor loginSeller) {
        checkSeller(loginSeller);

        Event event = eventRepository.findEventByIdJoinFetchSeller(id)
                .orElseThrow(() -> new EventException(NON_EXIST_EVENT));
        checkEventOwner(event, loginSeller.getId());
        deleteEvent(event);
    }

    private void deleteEvent(Event event) {
        sellerEventRepository.deleteAllByEventId(event.getId());
        eventRepository.delete(event);
    }

    private void updateEvent(Event event, EventUpdateReqDto dto) {
        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setCity(dto.getCity());
        event.setDistrict(dto.getDistrict());
        event.setThumbnailUrl(dto.getThumbnailUrl());
        event.setStartDate(dto.getStartDate());
        event.setEndDate(dto.getEndDate());
        event.setStatus(getCurrentStatus(dto.getStartDate(), dto.getEndDate()));
    }

    private EventStatus getCurrentStatus(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime now = LocalDateTime.now();
        if (endDate.isBefore(now)) {
            return EventStatus.END;
        }

        if (endDate.isAfter(now) && startDate.isBefore(now)) {
            return EventStatus.PROCEEDING;
        }

        return EventStatus.WAIT;
    }

    private void checkEventOwner(Event event, Long sellerId) {
        if (!event.getHost().getId().equals(sellerId)) {
            throw new EventException(NOT_MATCH_OWNER_OF_EVENT);
        }
    }

    private void checkSeller(Actor loginSeller) {
        if (loginSeller == null || loginSeller.getRole() != Role.ROLE_SELLER) {
            throw new EventException(NONE_VALID_LOGIN_SELLER);
        }
    }

    private Seller getSellerByLoginSeller(Actor loginSeller) {

        return sellerRepository.findById(loginSeller.getId())
                .orElseThrow(() -> new EventException(NON_EXIST_SELLER));
    }

    @Transactional(readOnly = true)
    public Page<EventRespDto> findAllByPage(Pageable pageable) {
        Page<Event> eventPage = eventRepository.findAll(pageable);

        return eventPage.map(EventRespDto::fromEntity);
    }

    @Transactional(readOnly = true)
    public EventDetailRespDto findEventDetail(Long id) {
        Event event = eventRepository.findEventByIdJoinFetchSeller(id)
                .orElseThrow(() -> new EventException(NON_EXIST_EVENT));

        return EventDetailRespDto.fromEntity(event);
    }

    @Transactional
    public void joinEvent(Long id, Actor loginSeller) {
        checkSeller(loginSeller);

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventException(NON_EXIST_EVENT));
        Seller seller = getSellerByLoginSeller(loginSeller);
        isAlreadyJoinSeller(event, seller);

        SellerEvent sellerEvent = new SellerEvent(seller, event);
        sellerEventRepository.save(sellerEvent);
    }

    private void isAlreadyJoinSeller(Event event, Seller seller) {
        boolean isAlreadyJoin = sellerEventRepository.existsByEventAndSeller(event, seller);

        if (isAlreadyJoin) {
            throw new EventException(ALREADY_JOIN_EVENT);
        }
    }

    @Transactional
    public void cancelJoinEvent(Long id, Actor loginSeller) {
        checkSeller(loginSeller);

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventException(NON_EXIST_EVENT));
        Seller seller = getSellerByLoginSeller(loginSeller);
        deleteSellerEvent(seller, event);
    }

    private void deleteSellerEvent(Seller seller, Event event) {
        if (!sellerEventRepository.existsByEventAndSeller(event, seller)) {
            throw new EventException(NOT_JOIN_EVENT);
        }
        sellerEventRepository.deleteByEventAndSeller(event, seller);
    }
}
