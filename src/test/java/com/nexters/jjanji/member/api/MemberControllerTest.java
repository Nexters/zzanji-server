package com.nexters.jjanji.member.api;

import com.nexters.jjanji.docs.RestDocs;
import com.nexters.jjanji.member.domain.Member;
import com.nexters.jjanji.member.domain.MemberRepository;
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
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
class MemberControllerTest extends RestDocs {

    final static String AUTHORIZATION_HEADER = "Authorization";
    final static String DEVICE_ID = "DEVICE_ID";
    MockMvc mockMvc;
    @Autowired HandlerInterceptor handlerInterceptor;
    @Autowired MemberRepository memberRepository;
    @Autowired MemberController memberController;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = getMockMvcBuilder(restDocumentation, memberController)
                .addInterceptors(handlerInterceptor)
                .build();
        memberRepository.save(Member.builder()
                .deviceId("DEVICE_ID")
                .build());
    }

    @Test
    @DisplayName("유저 테스트 API - 문서화 테스트")
    void addPhochak() throws Exception {

        //when, then
        mockMvc.perform(get("/member/test").header(AUTHORIZATION_HEADER, DEVICE_ID))
                .andExpect(status().isOk())
                .andDo(document("member/test",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER)
                                        .description("(필수) device id")
                        ),
                        responseFields(
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("응답값")
                        )
                ));
    }
}