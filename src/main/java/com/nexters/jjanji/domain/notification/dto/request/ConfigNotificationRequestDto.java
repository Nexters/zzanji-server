package com.nexters.jjanji.domain.notification.dto.request;

import com.nexters.jjanji.domain.notification.specification.OperatingSystem;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConfigNotificationRequestDto {
    private String fcmToken;
    private OperatingSystem operatingSystem;
    private int notificationHour;
    private int notificationMinute;

    @Builder
    public ConfigNotificationRequestDto(String fcmToken, OperatingSystem operatingSystem, int notificationHour, int notificationMinute) {
        this.fcmToken = fcmToken;
        this.operatingSystem = operatingSystem;
        this.notificationHour = notificationHour;
        this.notificationMinute = notificationMinute;
    }
}
