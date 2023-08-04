package com.nexters.jjanji.infra.pushnotification;

import com.nexters.jjanji.domain.notification.specification.OperatingSystem;
import com.nexters.jjanji.infra.pushnotification.dto.RequestPushDto;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * FCM 테스트 컨트롤러
 * 테스트 확인 시 삭제 예정
 */
@RestController
@RequiredArgsConstructor
public class FCMTestController {
    private final PushNotificationClient pushNotificationClient;
    @PostMapping("/fcm/test")
    public ResponseEntity fCMCloudMessageClientV2Test(@RequestBody RequestTestDto dto){

        pushNotificationClient.pushNotificationToClients(List.of(new RequestPushDto(OperatingSystem.IOS, dto.getToken())),PushMessage.CONSUMPTION_HISTORY);
        return ResponseEntity.ok().build();
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class RequestTestDto{
        String token;
    }
}
