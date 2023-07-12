package com.nexters.jjanji.domain.challenge.domain.repository;

import com.nexters.jjanji.domain.challenge.domain.SpendingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpendingHistoryRepository extends JpaRepository<SpendingHistory, Long> {
}
