package com.nexters.jjanji.domain.challenge.domain.repository;

import com.nexters.jjanji.domain.challenge.domain.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findByParticipationId(Long participationId);

    @Modifying
    @Query(value = "INSERT INTO plan (category, category_goal_amount, category_spend_amount, participation_id) " +
                "select plan.category, plan.category_goal_amount, :nextChallengeId, " +
                        "(select participation_id from participation inner_p where inner_p.challenge_id = :nextChallengeId and inner_p.member_id = p.member_id) " +
                "from plan " +
                "join participation p on p.participation_id = plan.participation_id " +
                "join challenge c on c.challenge_id = p.challenge_id " +
                "where c.challenge_id = :currentChallengeId",
            nativeQuery = true)
    void copyPreviousPlans(@Param("currentChallengeId") Long currentChallengeId, @Param("nextChallengeId") Long nextChallengeId);

}
