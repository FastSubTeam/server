package com.popple.server.domain.survey.service;

import com.popple.server.domain.entity.Survey;
import com.popple.server.domain.entity.SurveyOption;
import com.popple.server.domain.survey.dto.SurveyDetailRespDto;
import com.popple.server.domain.survey.dto.SurveyRespDto;
import com.popple.server.domain.survey.exception.RequestInvalidException;
import com.popple.server.domain.survey.repository.SurveyOptionRepository;
import com.popple.server.domain.survey.repository.SurveyRepository;
import com.popple.server.domain.survey.dto.OptionCreateDto;
import com.popple.server.domain.survey.dto.SurveyCreateReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.popple.server.domain.survey.type.SurveyStatus.WAIT;

@RequiredArgsConstructor
@Service
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final SurveyOptionRepository surveyOptionRepository;

    @Transactional
    public SurveyRespDto save(SurveyCreateReqDto dto) {
        Survey survey = Survey.builder()
                .title(dto.getTitle())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(WAIT) // TODO: 날짜에 따라서 상태를 어떻게 저장할지 결정
                .createdAt(Timestamp.valueOf(LocalDateTime.now())) // TODO: Auditing 변경
                .build();

        surveyRepository.save(survey);

        for (OptionCreateDto optionDto : dto.getOptions()) {
            SurveyOption option = SurveyOption.builder()
                    .content(optionDto.getContent())
                    .survey(survey)
                    .createdAt(Timestamp.valueOf(LocalDateTime.now())) // TODO: Auditing 변경
                    .build();

            surveyOptionRepository.save(option);
        }

        return SurveyRespDto.fromEntity(survey);
    }

    @Transactional(readOnly = true)
    public List<SurveyRespDto> findAll() {
        List<Survey> surveys = surveyRepository.findAll();

        return surveys.stream()
                .map(SurveyRespDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SurveyDetailRespDto findById(int id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new RequestInvalidException("찾고자하는 수요조사 정보가 존재하지 않습니다. : " + id));

        List<SurveyOption> options = surveyOptionRepository.findBySurveyId(survey.getId());

        return SurveyDetailRespDto.fromEntity(survey, options);
    }

    @Transactional
    public void deleteById(int id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new RequestInvalidException("삭제하려는 수요조사 정보가 존재하지 않습니다. : " + id));

        surveyOptionRepository.deleteBySurveyId(survey.getId());
        surveyRepository.deleteById(survey.getId());
    }
}
