package com.popple.server.domain.survey.repository;

import com.popple.server.domain.entity.SurveyOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyOptionRepository extends JpaRepository<SurveyOption, Integer> {
    List<SurveyOption> findBySurveyId(int surveyId);

    void deleteBySurveyId(int surveyId);
}
