package com.popple.server.domain.survey.repository;

import com.popple.server.domain.entity.Member;
import com.popple.server.domain.entity.Survey;
import com.popple.server.domain.entity.SurveyResult;
import com.popple.server.domain.survey.dto.SurveyResultDetailRespDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SurveyResultRepository extends JpaRepository<SurveyResult, Integer> {
    Optional<SurveyResult> findByMemberAndSurvey(Member member, Survey survey);
    @Query("SELECT s.id AS answerId, m.id AS userId, s.surveyOption.id AS optionId, s.age AS age, m.city AS area " +
            "FROM SurveyResult s " +
            "JOIN Member m " +
            "ON s.member.id = m.id")
    List<SurveyResultDetailRespDto.SurveyAnswer> findBySurveyId(Integer surveyId);
}
