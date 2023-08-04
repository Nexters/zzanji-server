package com.nexters.jjanji.domain.notification.presentation;

import com.nexters.jjanji.domain.notification.application.NotificationService;
import com.nexters.jjanji.domain.notification.dto.request.ConfigNotificationRequestDto;
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

    @PostMapping("/config")
    public ResponseEntity<Void> configNotification(@Valid @RequestBody ConfigNotificationRequestDto requestDto){
        String deviceId = MemberContext.getDevice();
        notificationService.configNotificationInfo(deviceId, requestDto);
        return ResponseEntity.ok().build();
    }

}
