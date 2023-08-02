package com.nexters.jjanji.domain.notification.domain;

import com.nexters.jjanji.domain.notification.specification.OperatingSystem;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class NotificationInfo {

    @Id
    private String deviceId;

    @Column(nullable = false)
    private String fcmToken;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OperatingSystem operatingSystem;

    @Column(nullable = false)
    private int notificationHour;

    @Column(nullable = false)
    private int notificationMinute;

    @Builder
    public NotificationInfo(String deviceId, String fcmToken, OperatingSystem operatingSystem, int notificationHour, int notificationMinute) {
        this.deviceId = deviceId;
        this.fcmToken = fcmToken;
        this.operatingSystem = operatingSystem;
        this.notificationHour = notificationHour;
        this.notificationMinute = notificationMinute;
    }
}
