package com.popple.server.domain.survey.service;

import com.popple.server.common.dto.APIDataResponse;
import com.popple.server.domain.entity.Member;
import com.popple.server.domain.entity.Survey;
import com.popple.server.domain.entity.SurveyOption;
import com.popple.server.domain.survey.dto.*;
import com.popple.server.domain.survey.exception.RequestInvalidException;
import com.popple.server.domain.survey.repository.SurveyOptionRepository;
import com.popple.server.domain.survey.repository.SurveyRepository;
import com.popple.server.domain.survey.repository.SurveyResultRepository;
import com.popple.server.domain.survey.type.SurveyStatus;
import com.popple.server.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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
    private final MemberRepository memberRepository;
    private final SurveyResultRepository surveyResultRepository;

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

    @Transactional(readOnly = true)
    public APIDataResponse<?> findActiveSurvey(Authentication authentication) {
        Survey survey = getActiveSurvey();
        if (survey == null) {
            return APIDataResponse.of(HttpStatus.OK, "진행중인 수요조사가 존재하지 않습니다.");
        }

        Member member = getLoginMember(authentication);
        Boolean isDone = isParticipation(member, survey);
        List<SurveyOption> surveyOptions = surveyOptionRepository.findBySurveyId(survey.getId());
        ActiveSurveyRespDto responseBody = ActiveSurveyRespDto.fromEntity(survey, surveyOptions, isDone);

        return APIDataResponse.of(HttpStatus.OK, responseBody);
    }

    /** @return 현재 진행 중인 수요로사를 반환. 없으면 null 반환. */
    private Survey getActiveSurvey() {
        return surveyRepository.findFirstByStatusOrderByStartDate(SurveyStatus.IN_PROGRESS)
                .orElse(null);
    }

    /** @return 현재 로그인되어 있는 유저를 반환. 없으면 null 반환 */
    private Member getLoginMember(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return null;
        }

        return memberRepository.findById(Long.valueOf(authentication.getName()))
                .orElse(null);
    }

    /** @return 해당 수요조사에 등록된 선택지들을 반환 */
    private List<SurveyOption> getSurveyOptions(Survey survey) {
        return surveyOptionRepository.findBySurveyId(survey.getId());
    }

    /** @return 사용자가 해당 수요조사에 참여했는지 여부를 반환 */
    private Boolean isParticipation(Member member, Survey survey) {
        if (member == null) {
            return false;
        }

        return surveyResultRepository.findByMemberAndSurvey(member, survey).isPresent();
    }

    @Transactional(readOnly = true)
    public APIDataResponse<?> getSurveyResults() {
        List<SurveyResultRespDto> surveyResults =
                surveyRepository.findFirst10ByStatusOrderByEndDateDesc(SurveyStatus.REVERT)
                        .stream()
                        .map(SurveyResultRespDto::fromEntity)
                        .collect(Collectors.toList());

        return APIDataResponse.of(HttpStatus.OK, surveyResults);
    }
}
