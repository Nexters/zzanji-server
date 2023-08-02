package com.nexters.jjanji.domain.notification.domain;

import com.nexters.jjanji.domain.notification.specification.OperatingSystem;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "idx_notification_info_hour", columnList = "notificationHour")
})
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
