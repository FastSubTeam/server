package com.popple.server.domain.survey.repository;

import com.popple.server.domain.entity.Survey;
import com.popple.server.domain.survey.type.SurveyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Integer> {
    List<Survey> findAllByStatusIsNot(SurveyStatus status);
    Optional<Survey> findFirstByStatusOrderByStartDate(SurveyStatus status);
    List<Survey> findFirst10ByStatusOrderByEndDateDesc(SurveyStatus status);
}
