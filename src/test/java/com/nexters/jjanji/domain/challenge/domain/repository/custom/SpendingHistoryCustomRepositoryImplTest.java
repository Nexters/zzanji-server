package com.nexters.jjanji.domain.challenge.domain.repository.custom;

import com.nexters.jjanji.domain.challenge.domain.Plan;
import com.nexters.jjanji.domain.challenge.domain.SpendingHistory;
import com.nexters.jjanji.domain.challenge.domain.repository.PlanRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.SpendingHistoryRepository;
import com.nexters.jjanji.domain.challenge.specification.PlanCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SpendingHistoryCustomRepositoryImplTest {
    @Autowired SpendingHistoryRepository spendingHistoryRepository;
    @Autowired PlanRepository planRepository;

    @Test
    @DisplayName("소비 내역 리스트 10개 중 두번째 최신 3개를 조회에 성공하다. - offset slice")
    @Transactional
    void findSpendingList_offsetSice(){
        //given
        final Plan plan = planRepository.save(Plan.builder().category(PlanCategory.FOOD).categoryGoalAmount(5000L).build());
        final SpendingHistory spending1 = createSpendingHistory(plan, "title1", "memo1", 1000L);
        final SpendingHistory spending2 = createSpendingHistory(plan, "title2", "memo2", 1000L);
        final SpendingHistory spending3 = createSpendingHistory(plan, "title3", "memo3", 1000L);
        final SpendingHistory spending4 = createSpendingHistory(plan, "title4", "memo4", 1000L);
        final SpendingHistory spending5 = createSpendingHistory(plan, "title5", "memo5", 1000L);
        final SpendingHistory spending6 = createSpendingHistory(plan, "title6", "memo6", 1000L);
        final SpendingHistory spending7 = createSpendingHistory(plan, "title7", "memo7", 1000L);
        final SpendingHistory spending8 = createSpendingHistory(plan, "title8", "memo8", 1000L);
        final SpendingHistory spending9 = createSpendingHistory(plan, "title9", "memo9", 1000L);
        final SpendingHistory spending10 = createSpendingHistory(plan, "title10", "memo10", 1000L);

        PageRequest page = PageRequest.of(1, 3);

        //when
        Slice<SpendingHistory> result = spendingHistoryRepository.findOffsetSliceByPlan(plan, page);

        //then
        assertThat(result.hasNext()).isTrue();
        List<SpendingHistory> contents = result.getContent();
        assertThat(contents.size()).isEqualTo(3);
        assertThat(contents.get(0).getId()).isEqualTo(spending7.getId());
        assertThat(contents.get(1).getId()).isEqualTo(spending6.getId());
        assertThat(contents.get(2).getId()).isEqualTo(spending5.getId());
    }

    @Test
    @DisplayName("소비 내역 리스트 10개 중 두번째 최신 3개를 조회에 성공하다. - cursor slice")
    @Transactional
    /**
     * 클래스에 선언하는 @Transactional vs 메서드에 선언하는 @Transactional ???????????????
     */
    void findSpendingList_cursorSlice(){
        //given
        final Plan plan = planRepository.save(Plan.builder().category(PlanCategory.FOOD).categoryGoalAmount(5000L).build());
        final SpendingHistory spending1 = createSpendingHistory(plan, "title1", "memo1", 1000L);
        final SpendingHistory spending2 = createSpendingHistory(plan, "title2", "memo2", 1000L);
        final SpendingHistory spending3 = createSpendingHistory(plan, "title3", "memo3", 1000L);
        final SpendingHistory spending4 = createSpendingHistory(plan, "title4", "memo4", 1000L);
        final SpendingHistory spending5 = createSpendingHistory(plan, "title5", "memo5", 1000L);
        final SpendingHistory spending6 = createSpendingHistory(plan, "title6", "memo6", 1000L);
        final SpendingHistory spending7 = createSpendingHistory(plan, "title7", "memo7", 1000L);
        final SpendingHistory spending8 = createSpendingHistory(plan, "title8", "memo8", 1000L);
        final SpendingHistory spending9 = createSpendingHistory(plan, "title9", "memo9", 1000L);
        final SpendingHistory spending10 = createSpendingHistory(plan, "title10", "memo10", 1000L);

        PageRequest page = PageRequest.ofSize(3);

        //when
        Slice<SpendingHistory> result = spendingHistoryRepository.findCursorSliceByPlan(spending8.getId(), plan, page);

        //then
        assertThat(result.hasNext()).isTrue();
        List<SpendingHistory> contents = result.getContent();
        assertThat(contents.size()).isEqualTo(3);
        assertThat(contents.get(0).getId()).isEqualTo(spending7.getId());
        assertThat(contents.get(1).getId()).isEqualTo(spending6.getId());
        assertThat(contents.get(2).getId()).isEqualTo(spending5.getId());
    }

    private SpendingHistory createSpendingHistory(Plan plan, String title, String memo, Long amount){
        SpendingHistory spendingHistory = SpendingHistory.builder()
                .plan(plan)
                .title(title)
                .memo(memo)
                .spendAmount(amount)
                .build();
        return spendingHistoryRepository.save(spendingHistory);
    }
}