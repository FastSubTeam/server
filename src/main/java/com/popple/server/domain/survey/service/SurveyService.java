package com.popple.server.domain.survey.service;

import com.popple.server.domain.entity.Survey;
import com.popple.server.domain.entity.SurveyOption;
import com.popple.server.domain.survey.dto.*;
import com.popple.server.domain.survey.exception.RequestInvalidException;
import com.popple.server.domain.survey.repository.SurveyOptionRepository;
import com.popple.server.domain.survey.repository.SurveyRepository;
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

    @Transactional
    public void updateSurvey(SurveyUpdateReqDto dto, int id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new RequestInvalidException("수정하려는 수요조사 정보가 존재하지 않습니다. : " + id));

        List<SurveyUpdateReqDto.OptionUpdateReqDto> options = dto.getOptions();
        findDeleteOptions(options, id);
        updateOptions(options, survey);

        survey.setTitle(dto.getTitle());
        survey.setStartDate(dto.getStartDate());
        survey.setEndDate(dto.getEndDate());
    }

    /** 삭제된 Options 탐색하여 제거: UpdateDto에 없는 optionId는 삭제 */
    private void findDeleteOptions(List<SurveyUpdateReqDto.OptionUpdateReqDto> options, int surveyId) {
        List<Integer> optionDtoIds = options.stream()
                .map(SurveyUpdateReqDto.OptionUpdateReqDto::getId).collect(Collectors.toList());

        List<SurveyOption> surveyOptions = surveyOptionRepository.findBySurveyId(surveyId);
        for (SurveyOption surveyOption : surveyOptions) {
            if (!optionDtoIds.contains(surveyOption.getId())) {
                surveyOptionRepository.delete(surveyOption);
            }
        }
    }

    private void updateOptions(List<SurveyUpdateReqDto.OptionUpdateReqDto> options, Survey survey) {
        for (SurveyUpdateReqDto.OptionUpdateReqDto optionDto : options) {
            if (optionDto.getId() != null) {
                int optionId = optionDto.getId();
                SurveyOption optionPS = surveyOptionRepository.findById(optionId)
                        .orElseThrow(() -> new RequestInvalidException("수정하려는 옵션 정보가 존재하지 않습니다. : " + optionId));
                optionPS.setContent(optionDto.getContent());
            } else {
                SurveyOption surveyOption = SurveyOption.builder()
                        .content(optionDto.getContent())
                        .survey(survey)
                        .createdAt(Timestamp.valueOf(LocalDateTime.now())) // TODO: Auditing 변경
                        .build();
                surveyOptionRepository.save(surveyOption);
            }
        }
    }
}
