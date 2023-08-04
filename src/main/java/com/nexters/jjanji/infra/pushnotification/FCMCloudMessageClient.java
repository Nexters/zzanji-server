package com.nexters.jjanji.infra.pushnotification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import com.nexters.jjanji.infra.pushnotification.dto.RequestPushDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Firebase Admin SDK 사용 버전
 * batch 푸시
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FCMCloudMessageClient implements PushNotificationClient{
    @Value("${fcm.key.path}")
    private String FCM_PRIVATE_KEY_PATH;
    //메시지만 권한 설정
    @Value("${fcm.key.scope}")
    private String fireBaseScope;

    //Admin 계정 인증 작업
    //TODO: 예외처리 필요
    @PostConstruct
    public void init(){
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(
                            GoogleCredentials
                                    .fromStream(new ClassPathResource(FCM_PRIVATE_KEY_PATH).getInputStream())
                                    .createScoped(List.of(fireBaseScope)))
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("[Firebase application has been initialized]");
            }
        } catch (IOException e) {
            log.error("[Firebase Init Fail] ", e);
        }
    }

    @Override
    public void pushNotificationToClients(List<RequestPushDto> dtos, PushMessage pushMessage) {
        //메시지 만들기
        List<Message> messages = makeMessages(dtos, pushMessage);

        try{
            //알림 발송
            BatchResponse response = FirebaseMessaging.getInstance().sendAll(messages);

            //요청에 대한 응답 처리
            //Token이 Invalid할 경우 예외가 발생하지 않고 response에 응답으로 오게됨.
            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();
                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        log.warn("Exception : {}, Fcm Token : {}", responses.get(i).getException(), dtos.get(i).getToken());
                    }
                }
            }
        } catch (FirebaseMessagingException e) {
            log.error("[Firebase Message Push Fail] ", e);
        }
    }

    private List<Message> makeMessages(List<RequestPushDto> dtos, PushMessage message){
        String title = message.getTitle();
        String content = message.getContent();

        //추후 System 별 분기 처리 필요시 수정하기
        return dtos.stream()
                .map(dto ->
                        Message.builder()
                                .putData("time", LocalDateTime.now().toString())
                                .setNotification(new Notification(title,content))
                                .setToken(dto.getToken())
                                .build())
                .collect(Collectors.toList());
    }
}
