package com.nexters.jjanji.infra.pushnotification;

import com.nexters.jjanji.domain.notification.specification.OperatingSystem;

import java.io.IOException;

public interface PushNotificationClient {
    public void sendNotificationTo(OperatingSystem system, String targetToken, String title, String content) throws IOException;

}
