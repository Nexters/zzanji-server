package com.nexters.jjanji.infra.pushnotification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.nexters.jjanji.domain.notification.specification.OperatingSystem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMCloudMessageClient implements PushNotificationClient {
    //HTTP v1 API 버전 사용
    //TODO: @Value로 분리하기
    private final String firebaseConfigPath = "firebase/firebase-adminsdk.json";
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/zzanz-8dc7d/messages:send";
    private final String scopes = "https://www.googleapis.com/auth/cloud-platform";
    private final String HEADER_PREFIX = "Bearer ";
    private final ObjectMapper objectMapper;

    //TODO: 예외 처리 필요!!!, throw 하지말기
    //메시지 전송
    @Override
    public void sendNotificationTo(OperatingSystem system, String targetToken, String title, String content) throws IOException {
        String message = makeMessage(targetToken, title, content);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, HEADER_PREFIX + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();
        log.info("FCM Message push result = ", response.body().string());
        log.info(response.toString());
    }

    //메세지 생성
    private String makeMessage(String targetToken, String title, String content) throws JsonProcessingException {
        FcmMessageDto fcmMessage = FcmMessageDto.builder()
                .message(FcmMessageDto.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessageDto.Notification.builder()
                                .title(title)
                                .body(content)
                                .image(null)
                                .build()
                        ).build())
                .validateOnly(false)
                .build();
        return objectMapper.writeValueAsString(fcmMessage);
    }
    //메세지 전송을 위한 접근 토큰 발급
    private String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of(scopes));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
