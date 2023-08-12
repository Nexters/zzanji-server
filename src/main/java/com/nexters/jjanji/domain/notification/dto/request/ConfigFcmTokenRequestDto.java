package com.nexters.jjanji.domain.notification.dto.request;

import com.nexters.jjanji.domain.notification.specification.OperatingSystem;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConfigFcmTokenRequestDto {
    private String fcmToken;
    private OperatingSystem operatingSystem;
}
