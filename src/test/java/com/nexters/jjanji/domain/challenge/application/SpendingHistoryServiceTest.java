package com.nexters.jjanji.domain.challenge.application;

import com.nexters.jjanji.domain.challenge.domain.Plan;
import com.nexters.jjanji.domain.challenge.domain.SpendingHistory;
import com.nexters.jjanji.domain.challenge.domain.repository.PlanRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.SpendingHistoryRepository;
import com.nexters.jjanji.domain.challenge.dto.request.SpendingSaveDto;
import com.nexters.jjanji.domain.challenge.dto.response.SpendingDetailResponse;
import com.nexters.jjanji.domain.challenge.specification.PlanCategory;
import com.nexters.jjanji.global.exception.PlanNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

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

}