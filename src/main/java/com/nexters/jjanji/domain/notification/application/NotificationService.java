package com.nexters.jjanji.domain.notification.application;

import com.nexters.jjanji.domain.notification.domain.NotificationInfo;
import com.nexters.jjanji.domain.notification.domain.repository.NotificationInfoRepository;
import com.nexters.jjanji.domain.notification.dto.request.ConfigNotificationRequestDto;
import com.nexters.jjanji.infra.pushnotification.dto.RequestPushDto;
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
    public void configNotificationInfo(String deviceId, ConfigNotificationRequestDto requestDto) {
        NotificationInfo notificationInfo = NotificationInfo.builder()
                .deviceId(deviceId)
                .fcmToken(requestDto.getFcmToken())
                .notificationHour(requestDto.getNotificationHour())
                .notificationMinute(requestDto.getNotificationMinute())
                .build();
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
