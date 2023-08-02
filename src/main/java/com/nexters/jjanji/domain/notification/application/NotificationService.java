package com.nexters.jjanji.domain.notification.application;

import com.nexters.jjanji.domain.notification.domain.NotificationInfo;
import com.nexters.jjanji.domain.notification.domain.repository.NotificationInfoRepository;
import com.nexters.jjanji.domain.notification.dto.request.ConfigNotificationRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationInfoRepository notificationInfoRepository;

    public void configNotificationInfo(String deviceId, ConfigNotificationRequestDto requestDto) {
        NotificationInfo notificationInfo = NotificationInfo.builder()
                .deviceId(deviceId)
                .fcmToken(requestDto.getFcmToken())
                .notificationHour(requestDto.getNotificationHour())
                .notificationMinute(requestDto.getNotificationMinute())
                .build();
        notificationInfoRepository.save(notificationInfo);
    }
}
