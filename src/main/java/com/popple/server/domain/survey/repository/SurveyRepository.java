package com.popple.server.domain.survey.repository;

import com.popple.server.domain.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Integer> {
}
