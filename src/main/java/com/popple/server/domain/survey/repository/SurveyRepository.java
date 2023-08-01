package com.popple.server.domain.survey.repository;

import com.popple.server.domain.entity.Survey;
import com.popple.server.domain.survey.type.SurveyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Integer> {
    List<Survey> findAllByStatusIsNot(SurveyStatus status);
}
