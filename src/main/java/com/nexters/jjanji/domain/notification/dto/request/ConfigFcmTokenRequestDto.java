package com.nexters.jjanji.domain.notification.dto.request;

import com.nexters.jjanji.domain.notification.specification.OperatingSystem;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConfigFcmTokenRequestDto {
    private String fcmToken;
    private OperatingSystem operatingSystem;

    @Builder
    public ConfigFcmTokenRequestDto(final String fcmToken, final OperatingSystem operatingSystem) {
        this.fcmToken = fcmToken;
        this.operatingSystem = operatingSystem;
    }
}
