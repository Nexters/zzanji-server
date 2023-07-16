package com.nexters.jjanji.domain.challenge.domain.repository;

import com.nexters.jjanji.domain.challenge.domain.Plan;
import com.nexters.jjanji.domain.challenge.domain.SpendingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpendingHistoryRepository extends JpaRepository<SpendingHistory, Long> {

    List<SpendingHistory> findByPlan(Plan plan);
}
