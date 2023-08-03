package com.nexters.jjanji.domain.notification.presentation;

import com.nexters.jjanji.domain.notification.application.NotificationService;
import com.nexters.jjanji.domain.notification.dto.request.ConfigNotificationRequestDto;
import com.nexters.jjanji.global.auth.MemberContext;
import com.nexters.jjanji.infra.pushnotification.PushNotificationClient;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/v1/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/config")
    public ResponseEntity<Void> configNotification(@Valid @RequestBody ConfigNotificationRequestDto requestDto){
        String deviceId = MemberContext.getDevice();
        notificationService.configNotificationInfo(deviceId, requestDto);
        return ResponseEntity.ok().build();
    }



    private final PushNotificationClient pushNotificationClient;
    @PostMapping("/fcm/test")
    public ResponseEntity pushMessageTest(@RequestBody FcmRequestDto dto) throws IOException {

        pushNotificationClient.sendNotificationTo(null, dto.getToken(), dto.getTitle(), dto.getContent());
        return ResponseEntity.ok().build();
    }
    @Builder
    @Getter
    static class FcmRequestDto{
        private String token;
        private String title;
        private String content;

    }

}
