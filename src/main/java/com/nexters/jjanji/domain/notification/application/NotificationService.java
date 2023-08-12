package com.nexters.jjanji.domain.notification.application;

import com.nexters.jjanji.domain.notification.domain.NotificationInfo;
import com.nexters.jjanji.domain.notification.domain.NotificationTime;
import com.nexters.jjanji.domain.notification.domain.repository.NotificationInfoRepository;
import com.nexters.jjanji.domain.notification.domain.repository.NotificationTimeRepository;
import com.nexters.jjanji.domain.notification.dto.request.ConfigFcmTokenRequestDto;
import com.nexters.jjanji.domain.notification.dto.request.ConfigNotificationTimeRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationInfoRepository notificationInfoRepository;

    public void registerFcmToken(String deviceId, ConfigFcmTokenRequestDto requestDto) {
        final NotificationInfo notificationInfo = notificationInfoRepository.findById(deviceId).orElse(
                    NotificationInfo.builder()
                            .deviceId(deviceId)
                            .operatingSystem(requestDto.getOperatingSystem())
                            .build());
        notificationInfoRepository.save(notificationInfo);
    }

    public void configNotificationTime(String deviceId, ConfigNotificationTimeRequestDto requestDto) {
        // TODO 커스텀 예외 처리
        final NotificationInfo notificationInfo = notificationInfoRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalStateException("FCM 토큰이 등록되지 않았습니다."));
        notificationInfo.updateNotificationTime(requestDto.getNotificationHour(), requestDto.getNotificationMinute());
        notificationInfoRepository.save(notificationInfo);
    }
}
