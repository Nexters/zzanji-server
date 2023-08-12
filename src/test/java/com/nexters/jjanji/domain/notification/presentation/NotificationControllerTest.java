package com.nexters.jjanji.domain.notification.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.jjanji.docs.RestDocs;
import com.nexters.jjanji.domain.member.domain.Member;
import com.nexters.jjanji.domain.member.domain.MemberRepository;
import com.nexters.jjanji.domain.notification.dto.request.ConfigFcmTokenRequestDto;
import com.nexters.jjanji.domain.notification.dto.request.ConfigNotificationTimeRequestDto;
import com.nexters.jjanji.domain.notification.specification.OperatingSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
class NotificationControllerTest extends RestDocs {

    final static String AUTHORIZATION_HEADER = "Authorization";
    final static String DEVICE_ID = "DEVICE_ID";
    @Autowired HandlerInterceptor handlerInterceptor;
    @Autowired ObjectMapper objectMapper;
    @Autowired MemberRepository memberRepository;
    @Autowired NotificationController notificationController;
    MockMvc mockMvc;
    Long testMemberId;
    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = getMockMvcBuilder(restDocumentation, notificationController)
                .addInterceptors(handlerInterceptor)
                .build();
        Member member = memberRepository.save(Member.builder()
                .deviceId("DEVICE_ID")
                .build());
        testMemberId = member.getId();
    }

    @Test
    @DisplayName("푸시알람 API - FCM 토큰 등록 설정(변경)")
    void registerFcmToken() throws Exception {
        //given
        ConfigFcmTokenRequestDto requestDto = ConfigFcmTokenRequestDto.builder()
                .fcmToken("SOME_FCM_TOKEN")
                .operatingSystem(OperatingSystem.ANDROID)
                .build();

        //when, then
        mockMvc.perform(post("/v1/notification/register")
                        .header(AUTHORIZATION_HEADER, DEVICE_ID)
                        .header("Content-Type", "application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(document("notification/register/POST",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER)
                                        .description("(필수) device id")
                        ),
                        requestFields(
                                fieldWithPath("fcmToken").type(JsonFieldType.STRING).description("fcm 토큰"),
                                fieldWithPath("operatingSystem").type(JsonFieldType.STRING).description("운영체제 - IOS, ANDROID")
                        )
                ));
    }

    @Test
    @DisplayName("푸시알람 API - 푸시 알람 시간 설정(변경)")
    void configNotificationTime() throws Exception {
        //given
        ConfigNotificationTimeRequestDto requestDto = ConfigNotificationTimeRequestDto.builder()
                .notificationHour(5)
                .notificationMinute(10)
                .build();

        //when, then
        mockMvc.perform(post("/v1/notification/register")
                        .header(AUTHORIZATION_HEADER, DEVICE_ID)
                        .header("Content-Type", "application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(document("notification/register/POST",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER)
                                        .description("(필수) device id")
                        ),
                        requestFields(
                                fieldWithPath("notificationHour").type(JsonFieldType.NUMBER).description("알림 시간 시 - 24시"),
                                fieldWithPath("notificationMinute").type(JsonFieldType.NUMBER).description("알림 시간 분 - 60분")
                        )
                ));
    }
}