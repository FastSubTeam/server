package com.popple.server.domain.survey.repository;

import com.popple.server.domain.entity.Member;
import com.popple.server.domain.entity.Survey;
import com.popple.server.domain.entity.SurveyResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SurveyResultRepository extends JpaRepository<SurveyResult, Integer> {
    Optional<SurveyResult> findByMemberAndSurvey(Member member, Survey survey);
}
