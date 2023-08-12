package com.nexters.jjanji.domain.notification.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConfigNotificationTimeRequestDto {
    private int notificationHour;
    private int notificationMinute;

    @Builder
    public ConfigNotificationTimeRequestDto(final int notificationHour, final int notificationMinute) {
        this.notificationHour = notificationHour;
        this.notificationMinute = notificationMinute;
    }
}
