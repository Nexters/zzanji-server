package com.nexters.jjanji.domain.notification.domain.repository;

import com.nexters.jjanji.domain.notification.domain.NotificationInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationInfoRepository extends JpaRepository<NotificationInfo, String> {
}
