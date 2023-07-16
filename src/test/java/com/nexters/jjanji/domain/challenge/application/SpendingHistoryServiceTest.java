package com.nexters.jjanji.domain.challenge.application;

import com.nexters.jjanji.domain.challenge.domain.Plan;
import com.nexters.jjanji.domain.challenge.domain.repository.PlanRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.SpendingHistoryRepository;
import com.nexters.jjanji.domain.challenge.dto.request.SpendingSaveDto;
import com.nexters.jjanji.domain.challenge.specification.PlanCategory;
import com.nexters.jjanji.global.exception.NotExistPlanException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        final Plan plan = Plan.builder().category(PlanCategory.FOOD).categoryGoalAmount(10000L).build();
        given(planRepository.findById(1L))
                .willReturn(Optional.of(plan));

        SpendingSaveDto saveDto = new SpendingSaveDto("title", "content", 10000L);

        //when
        spendingHistoryService.addSpendingHistory(1L, saveDto);

        //then
        verify(planRepository).findById(1L);
        verify(spendingHistoryRepository).save(any());
    }

    @Test
    @DisplayName("지출 내역 등록시 계획된 카테고리가 없어 예외가 발생한다.")
    void addSpendingHistory_notExistPlan(){
        //given
        SpendingSaveDto saveDto = new SpendingSaveDto("title", "content", 10000L);

        //when & then
        assertThatThrownBy(() -> {
            spendingHistoryService.addSpendingHistory(1L, saveDto);
        }).isInstanceOf(NotExistPlanException.class);
    }
}