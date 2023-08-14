package com.nexters.jjanji.infra.pushnotification.dto;

import com.nexters.jjanji.domain.notification.domain.NotificationInfo;
import com.nexters.jjanji.domain.notification.specification.OperatingSystem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestPushDto {
    private OperatingSystem system;
    private String token;

    public static RequestPushDto from(NotificationInfo info){
        RequestPushDto dto = new RequestPushDto();
        dto.system = info.getOperatingSystem();
        dto.token = info.getFcmToken();
        return dto;
    }
}
