package com.popple.server.domain.event.component;

import com.popple.server.domain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventScheduler {

    private final EventRepository eventRepository;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void eventScheduling() {
        log.info("[{}] 스케쥴러 작동 행사 데이터 정리", LocalDateTime.now());

        String now = LocalDate.now().toString();
        eventRepository.updateWaitEvent(now);
        eventRepository.updateProceedingEvent(now);
        eventRepository.updateEndEvent(now);
    }
}
