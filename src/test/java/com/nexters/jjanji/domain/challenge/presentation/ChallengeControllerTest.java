package com.nexters.jjanji.domain.challenge.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.jjanji.docs.RestDocs;
import com.nexters.jjanji.domain.challenge.dto.request.ParticipateRequestDto;
import com.nexters.jjanji.domain.member.domain.Member;
import com.nexters.jjanji.domain.member.domain.MemberRepository;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
class ChallengeControllerTest extends RestDocs {

    final static String AUTHORIZATION_HEADER = "Authorization";
    final static String DEVICE_ID = "DEVICE_ID";
    MockMvc mockMvc;
    @Autowired
    HandlerInterceptor handlerInterceptor;
    @Autowired ObjectMapper objectMapper;
    @Autowired
    MemberRepository memberRepository;
    @Autowired ChallengeController challengeController;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = getMockMvcBuilder(restDocumentation, challengeController)
                .addInterceptors(handlerInterceptor)
                .build();
        memberRepository.save(Member.builder()
                .deviceId("DEVICE_ID")
                .build());
    }

    @Test
    @DisplayName("챌린지 API - 다음 챌린지 등록")
    void participateNextChallenge() throws Exception {
        //given
        ParticipateRequestDto requestDto = ParticipateRequestDto.builder()
                .goalAmount(10000L)
                .build();

        //when, then
        mockMvc.perform(post("/v1/challenge/participate")
                .header(AUTHORIZATION_HEADER, DEVICE_ID)
                .header("Content-Type", "application/json")
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(document("challenge/participate/POST",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER)
                                        .description("(필수) device id")
                        ),
                        requestFields(
                                fieldWithPath("goalAmount").type(JsonFieldType.NUMBER).description("목표 금액")
                        )
                ));
    }
}