package com.nexters.jjanji.domain.notification.presentation;

import com.nexters.jjanji.domain.notification.application.NotificationService;
import com.nexters.jjanji.domain.notification.dto.request.ConfigFcmTokenRequestDto;
import com.nexters.jjanji.domain.notification.dto.request.ConfigNotificationTimeRequestDto;
import com.nexters.jjanji.global.auth.MemberContext;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerFcmToken(@Valid @RequestBody ConfigFcmTokenRequestDto requestDto){
        String deviceId = MemberContext.getDevice();
        notificationService.registerFcmToken(deviceId, requestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/time")
    public ResponseEntity<Void> configNotificationTime(@Valid @RequestBody ConfigNotificationTimeRequestDto requestDto){
        String deviceId = MemberContext.getDevice();
        notificationService.configNotificationTime(deviceId, requestDto);
        return ResponseEntity.ok().build();
    }

}
