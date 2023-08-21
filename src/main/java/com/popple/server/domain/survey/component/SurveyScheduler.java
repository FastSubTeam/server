package com.popple.server.domain.survey.component;

import com.popple.server.domain.entity.Survey;
import com.popple.server.domain.survey.repository.SurveyRepository;
import com.popple.server.domain.survey.type.SurveyStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class SurveyScheduler {

    private final SurveyRepository surveyRepository;

    /* 매일 00시 05분에 설문조사 스케줄러 작동 */
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void surveyScheduling() {
        log.info("[{}] 스케쥴러 작동 설문조사 데이터 정리", LocalDateTime.now());

        LocalDate now = LocalDate.now();
        List<Survey> surveys = surveyRepository.findAllByStatusIsNot(SurveyStatus.REVERT);

        for (Survey survey : surveys) {
            if (isEnd(survey.getEndDate(), now)) {
                survey.setStatus(SurveyStatus.REVERT);
            }
            if (isStart(survey.getStartDate(), now)) {
                survey.setStatus(SurveyStatus.IN_PROGRESS);
            }
        }
    }

    private boolean isEnd(LocalDateTime endDate, LocalDate now) {
        return endDate.toLocalDate().isBefore(now);
    }

    private boolean isStart(LocalDateTime startDate, LocalDate now) {
        return startDate.toLocalDate().isEqual(now);
    }
}
