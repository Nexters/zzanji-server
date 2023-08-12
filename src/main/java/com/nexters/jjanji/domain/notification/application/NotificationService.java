package com.nexters.jjanji.domain.notification.application;

import com.nexters.jjanji.domain.notification.domain.NotificationInfo;
import com.nexters.jjanji.domain.notification.domain.repository.NotificationInfoRepository;
import com.nexters.jjanji.infra.pushnotification.dto.RequestPushDto;
import com.nexters.jjanji.domain.notification.dto.request.ConfigFcmTokenRequestDto;
import com.nexters.jjanji.domain.notification.dto.request.ConfigNotificationTimeRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {
    private final NotificationInfoRepository notificationInfoRepository;

    @Transactional
    public void registerFcmToken(String deviceId, ConfigFcmTokenRequestDto requestDto) {
        final NotificationInfo notificationInfo = notificationInfoRepository.findById(deviceId).orElse(
                    NotificationInfo.builder()
                            .deviceId(deviceId)
                            .operatingSystem(requestDto.getOperatingSystem())
                            .build());
        notificationInfoRepository.save(notificationInfo);
    }

    @Transactional
    public void configNotificationTime(String deviceId, ConfigNotificationTimeRequestDto requestDto) {
        // TODO 커스텀 예외 처리
        final NotificationInfo notificationInfo = notificationInfoRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalStateException("FCM 토큰이 등록되지 않았습니다."));
        notificationInfo.updateNotificationTime(requestDto.getNotificationHour(), requestDto.getNotificationMinute());
        notificationInfoRepository.save(notificationInfo);
    }

    public List<RequestPushDto> getCurrentTimeRequestPushDtos() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        return notificationInfoRepository.findByNotificationHourAndAndNotificationMinute(hour, minute).stream()
                .map(info -> RequestPushDto.from(info))
                .collect(Collectors.toList());
    }

}
