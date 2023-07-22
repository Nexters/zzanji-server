package com.nexters.jjanji.domain.challenge.domain.repository.custom;

import com.nexters.jjanji.domain.challenge.domain.Plan;
import com.nexters.jjanji.domain.challenge.domain.QSpendingHistory;
import com.nexters.jjanji.domain.challenge.domain.SpendingHistory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nexters.jjanji.domain.challenge.domain.QSpendingHistory.spendingHistory;

@Repository
@RequiredArgsConstructor
public class SpendingHistoryCustomRepositoryImpl implements SpendingHistoryCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * Slice 페이지네이션 기법(count 쿼리가 아닌 limit 방식)
     * offset 방식 사용 버전
     */
    @Override
    public Slice<SpendingHistory> findOffsetSliceByPlan(Plan plan, Pageable pageable) {
        List<SpendingHistory> content = jpaQueryFactory.selectFrom(spendingHistory)
                .where(spendingHistory.plan.eq(plan))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(spendingHistory.id.desc()) //최신순
                .fetch(); //counting 쿼리 x

        return checkEndPage(pageable, content);
    }

    /**
     * Slice 페이지네이션 기법(count 쿼리가 아닌 limit 방식)
     * no-offset 방식 사용 버전
     */
    @Override
    public Slice<SpendingHistory> findCursorSliceByPlan(Plan plan, Long lastSpendingId, Pageable pageable) {
        List<SpendingHistory> content = jpaQueryFactory.selectFrom(spendingHistory)
                .where(
                        spendingHistory.plan.eq(plan),
                        ltCursorId(lastSpendingId))
                .limit(pageable.getPageSize() + 1)
                .orderBy(spendingHistory.id.desc()) //최신순
                .fetch(); //counting 쿼리 x

        return checkEndPage(pageable, content);
    }


    private Slice<SpendingHistory> checkEndPage(Pageable pageable, List<SpendingHistory> content) {
        boolean hasNext = false;

        if(content.size() > pageable.getPageSize()){ //다음 페이지가 존재하는 경우
            content.remove(pageable.getPageSize()); //한개더 가져온 엔티티를 삭제
            hasNext = true;
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }
    private BooleanExpression ltCursorId(Long spendingId){
        return (spendingId!=null)?spendingHistory.id.lt(spendingId):null;
    }
}
