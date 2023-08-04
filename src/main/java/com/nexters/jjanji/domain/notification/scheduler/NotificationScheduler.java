package com.nexters.jjanji.domain.notification.scheduler;

import com.nexters.jjanji.domain.notification.application.NotificationService;
import com.nexters.jjanji.infra.pushnotification.PushMessage;
import com.nexters.jjanji.infra.pushnotification.PushNotificationClient;
import com.nexters.jjanji.infra.pushnotification.dto.RequestPushDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationScheduler {

    private final NotificationService notificationService;
    private final PushNotificationClient pushNotificationClient;

    /**
     * FCM 테스트 성공시 활성화 예정
     */
//    @Scheduled(cron = "0 0/1 * * * *", zone = "Asia/Seoul")
//    public void consumptionHistoryAlarm() {
//        log.info("소비 내역 입력 알람");
//        List<RequestPushDto> dtos = notificationService.getCurrentTimeRequestPushDtos();
//        pushNotificationClient.pushNotificationToClients(dtos, PushMessage.CONSUMPTION_HISTORY);
//    }
}
