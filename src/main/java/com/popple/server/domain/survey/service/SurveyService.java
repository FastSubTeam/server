package com.popple.server.domain.survey.service;

import com.popple.server.domain.entity.Survey;
import com.popple.server.domain.entity.SurveyOption;
import com.popple.server.domain.survey.repository.SurveyOptionRepository;
import com.popple.server.domain.survey.repository.SurveyRepository;
import com.popple.server.domain.survey.dto.OptionCreateDto;
import com.popple.server.domain.survey.dto.SurveyCreateReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static com.popple.server.domain.survey.type.SurveyStatus.WAIT;

@RequiredArgsConstructor
@Service
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final SurveyOptionRepository surveyOptionRepository;

    @Transactional
    public Survey save(SurveyCreateReqDto dto) {
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

        return survey;
    }
}
