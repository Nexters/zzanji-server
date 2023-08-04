package com.nexters.jjanji.domain.notification.domain.repository;

import com.nexters.jjanji.domain.notification.domain.NotificationInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationInfoRepository extends JpaRepository<NotificationInfo, String> {

    List<NotificationInfo> findByNotificationHourAndAndNotificationMinute(int hour, int minute);
}
