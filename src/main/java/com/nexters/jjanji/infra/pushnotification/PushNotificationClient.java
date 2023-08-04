package com.nexters.jjanji.infra.pushnotification;

import com.nexters.jjanji.infra.pushnotification.dto.RequestPushDto;

import java.util.List;

public interface PushNotificationClient {
    public void pushNotificationToClients(List<RequestPushDto> dtos, PushMessage pushMessage);
}
