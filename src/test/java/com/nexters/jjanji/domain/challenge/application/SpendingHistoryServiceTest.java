package com.nexters.jjanji.domain.challenge.application;

import com.nexters.jjanji.domain.challenge.domain.Challenge;
import com.nexters.jjanji.domain.challenge.domain.Plan;
import com.nexters.jjanji.domain.challenge.domain.SpendingHistory;
import com.nexters.jjanji.domain.challenge.domain.repository.ChallengeRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.PlanRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.SpendingHistoryRepository;
import com.nexters.jjanji.domain.challenge.dto.request.SpendingEditDto;
import com.nexters.jjanji.domain.challenge.dto.request.SpendingSaveDto;
import com.nexters.jjanji.domain.challenge.dto.response.SpendingDetailResponse;
import com.nexters.jjanji.domain.challenge.specification.PlanCategory;
import com.nexters.jjanji.global.exception.PlanNotFoundException;
import com.nexters.jjanji.global.exception.SpendingPeriodInvalidException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpendingHistoryServiceTest {

    @Mock PlanRepository planRepository;
    @Mock SpendingHistoryRepository spendingHistoryRepository;
    @Mock ChallengeRepository challengeRepository;
    @InjectMocks SpendingHistoryService spendingHistoryService;

    @Test
    @DisplayName("지출 내역 등록에 성공하다.")
    void addSpendingHistory(){
        //given
        Plan plan = mock(Plan.class);
        given(planRepository.findById(1L))
                .willReturn(Optional.of(plan));

        SpendingSaveDto saveDto = new SpendingSaveDto("title", "content", 1000L);

        //when
        spendingHistoryService.addSpendingHistory(1L, saveDto);

        //then
        verify(planRepository).findById(1L);
        verify(spendingHistoryRepository).save(any(SpendingHistory.class));
        verify(plan).plusCategorySpendAmount(1000L);
    }

    @Test
    @DisplayName("지출 내역 등록시 계획된 카테고리가 없어 예외가 발생한다.")
    void addSpendingHistory_notExistPlan(){
        //given
        SpendingSaveDto saveDto = new SpendingSaveDto("title", "content", 10000L);

        //when & then
        assertThatThrownBy(() -> {
            spendingHistoryService.addSpendingHistory(1L, saveDto);
        }).isInstanceOf(PlanNotFoundException.class);
    }

    @Test
    @DisplayName("Plan의 지출 내역 리스트 조회에 성공하다.")
    void findSpendingList(){
        //given
        Plan plan = Plan.builder().category(PlanCategory.FOOD).categoryGoalAmount(3000L).build();
        SpendingHistory spending1 = SpendingHistory.builder().plan(plan).title("커피").memo("스타벅스").spendAmount(1000L).build();
        SpendingHistory spending2 = SpendingHistory.builder().plan(plan).title("커피").memo("투썸").spendAmount(2000L).build();
        PageRequest pageRequest = PageRequest.ofSize(3);
        List<SpendingHistory> content = List.of(spending1, spending2);

        given(planRepository.findById(1L))
                .willReturn(Optional.of(plan));
        given(spendingHistoryRepository.findCursorSliceByPlan(plan, 1L, pageRequest))
                .willReturn(new SliceImpl<>(content, pageRequest, true));

        //when
        SpendingDetailResponse spendingList = spendingHistoryService.findSpendingList(1L, 1L, pageRequest);

        //then
        assertAll(
                () -> assertThat(spendingList.getGoalAmount()).isEqualTo(plan.getCategoryGoalAmount()),
                () -> assertThat(spendingList.getSpendAmount()).isEqualTo(plan.getCategorySpendAmount()),
                () -> assertThat(spendingList.getSpendingList().size()).isEqualTo(2),
                () -> assertThat(spendingList.getSpendingList().get(0).getTitle()).isEqualTo(spending1.getTitle()),
                () -> assertThat(spendingList.getSpendingList().get(0).getMemo()).isEqualTo(spending1.getMemo()),
                () -> assertThat(spendingList.getSpendingList().get(0).getSpendAmount()).isEqualTo(spending1.getSpendAmount()),
                () -> assertThat(spendingList.getSpendingList().get(1).getTitle()).isEqualTo(spending2.getTitle()),
                () -> assertThat(spendingList.getSpendingList().get(1).getMemo()).isEqualTo(spending2.getMemo()),
                () -> assertThat(spendingList.getSpendingList().get(1).getSpendAmount()).isEqualTo(spending2.getSpendAmount()),
                () -> assertThat(spendingList.getHasNext()).isTrue()
        );

        //verify
        verify(planRepository, times(1)).findById(1L);
        verify(spendingHistoryRepository, times(1)).findCursorSliceByPlan(plan, 1L, pageRequest);
    }

    @Test
    @DisplayName("지출 내역 수정에 성공하다.")
    void editSpendingHistory(){
        //given
        final Challenge challenge = mock(Challenge.class);
        final Plan plan = mock(Plan.class);
        final SpendingHistory spending = mock(SpendingHistory.class);
        final Long categorySpendAmount = 5000L;
        final Long spendAmount = 3000L;

        given(challengeRepository.findChallengeByPlanId(1L))
                .willReturn(Optional.of(challenge));
        given(planRepository.findById(1L))
                .willReturn(Optional.of(plan));
        given(spendingHistoryRepository.findById(1L))
                .willReturn(Optional.of(spending));

        given(challenge.isOpenedChallenge())
                .willReturn(true);
        given(plan.getCategorySpendAmount())
                .willReturn(categorySpendAmount);
        given(spending.getSpendAmount())
                .willReturn(spendAmount);

        final SpendingEditDto dto = new SpendingEditDto("엔제리너스", "부모님", 1000L);

        //when
        Long spendingId = spendingHistoryService.editSpendingHistory(1L, 1L, dto);

        //then
        verify(challengeRepository, times(1)).findChallengeByPlanId(1L);
        verify(planRepository, times(1)).findById(1L);
        verify(spendingHistoryRepository, times(1)).findById(1L);
        verify(challenge, times(1)).isOpenedChallenge();
        verify(plan,times(1)).getCategorySpendAmount();
        verify(spending,times(1)).getSpendAmount();
        verify(plan,times(1)).updateCategorySpendAmount(categorySpendAmount-spendAmount+dto.getSpendAmount());
    }

    @Test
    @DisplayName("종료된 챌린지에 지출 내역 입력을 시도하다.")
    void editSpendingHistory_finishChallenge(){
        //given
        final Challenge challenge = mock(Challenge.class);
        final SpendingEditDto dto = mock(SpendingEditDto.class);

        given(challengeRepository.findChallengeByPlanId(1L))
                .willReturn(Optional.of(challenge));
        given(challenge.isOpenedChallenge())
                .willReturn(false);

        //when & then
        assertThatThrownBy(() -> spendingHistoryService.editSpendingHistory(1L, 1L, dto))
                .isInstanceOf(SpendingPeriodInvalidException.class);
    }
}