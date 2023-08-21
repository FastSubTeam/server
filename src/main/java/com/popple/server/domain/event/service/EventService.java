package com.popple.server.domain.event.service;

import com.popple.server.domain.entity.Event;
import com.popple.server.domain.entity.Seller;
import com.popple.server.domain.event.dto.EventCreateReqDto;
import com.popple.server.domain.event.dto.EventDetailRespDto;
import com.popple.server.domain.event.dto.EventRespDto;
import com.popple.server.domain.event.exception.EventException;
import com.popple.server.domain.event.repository.EventRepository;
import com.popple.server.domain.user.repository.SellerRepository;
import com.popple.server.domain.user.vo.Actor;
import com.popple.server.domain.user.vo.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.popple.server.domain.event.exception.EventExceptionMessage.*;

@RequiredArgsConstructor
@Service
public class EventService {
    private final EventRepository eventRepository;
    private final SellerRepository sellerRepository;

    @Transactional
    public void save(EventCreateReqDto dto, Actor loginSeller) {

        // TODO: 토큰 기능 완료되면 그때 활성화
        checkSeller(loginSeller);
        Seller seller = getSellerByLoginSeller(loginSeller);
        Event event = dto.toEntity(seller);
        eventRepository.save(event);
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
}
