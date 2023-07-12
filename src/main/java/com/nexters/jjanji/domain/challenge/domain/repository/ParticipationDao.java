package com.nexters.jjanji.domain.challenge.domain.repository;

import com.nexters.jjanji.domain.challenge.dto.PrevAndNextPostVO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class ParticipationDao {

    @PersistenceContext
    private EntityManager entityManager;

    public PrevAndNextPostVO getPreviousAndNextPostIds(Long memberId, Long participationId) {
        String query = "SELECT " +
                "MAX(CASE WHEN participation_id < :participationId THEN participation_id END) AS previous_post_id, " +
                "MIN(CASE WHEN participation_id > :participationId THEN participation_id END) AS next_post_id " +
                "FROM participation " +
                "WHERE member_id = :memberId";

        Object[] queryResult = (Object[]) entityManager.createNativeQuery(query)
                .setParameter("memberId", memberId)
                .setParameter("participationId", participationId)
                .getSingleResult();

        return PrevAndNextPostVO.queryResultToObject(queryResult);
    }
}
