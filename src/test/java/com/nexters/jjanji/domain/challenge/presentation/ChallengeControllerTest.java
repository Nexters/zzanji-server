package com.nexters.jjanji.domain.challenge.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.jjanji.docs.RestDocs;
import com.nexters.jjanji.domain.challenge.domain.Challenge;
import com.nexters.jjanji.domain.challenge.domain.Participation;
import com.nexters.jjanji.domain.challenge.domain.Plan;
import com.nexters.jjanji.domain.challenge.domain.repository.ChallengeRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.ParticipationRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.PlanRepository;
import com.nexters.jjanji.domain.challenge.dto.request.CreateCategoryPlanRequestDto;
import com.nexters.jjanji.domain.challenge.dto.request.ParticipateRequestDto;
import com.nexters.jjanji.domain.challenge.specification.PlanCategory;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
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
    @Autowired ParticipationRepository participationRepository;
    @Autowired ChallengeRepository challengeRepository;
    @Autowired PlanRepository planRepository;

    Long testMemberId;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = getMockMvcBuilder(restDocumentation, challengeController)
                .addInterceptors(handlerInterceptor)
                .build();
        Member member = memberRepository.save(Member.builder()
                .deviceId("DEVICE_ID")
                .build());
        testMemberId = member.getId();
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

    @Test
    @DisplayName("챌린지 API - 카테고리별 목표 등록")
    void addCategoryPlan() throws Exception {
        //given
        Challenge challenge = challengeRepository.save(
                Challenge.builder()
                        .startAt(LocalDateTime.now())
                        .endAt(LocalDateTime.now().plusDays(7))
                        .build()
        );

        participationRepository.save(
                Participation.builder()
                        .challenge(challenge)
                        .goalAmount(10000L)
                        .member(memberRepository.getReferenceById(testMemberId))
                        .build()
        );

        List<CreateCategoryPlanRequestDto> request = List.of(
                CreateCategoryPlanRequestDto.builder()
                        .category(PlanCategory.TRANSPORTATION)
                        .goalAmount(10000L)
                        .build(),
                CreateCategoryPlanRequestDto.builder()
                        .category(PlanCategory.FOOD)
                        .goalAmount(200000L)
                        .build()
        );

        //when, then
        mockMvc.perform(post("/v1/challenge/plan/category")
                .header(AUTHORIZATION_HEADER, DEVICE_ID)
                .header("Content-Type", "application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("challenge/plan/category/POST",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER)
                                        .description("(필수) device id")
                        ),
                        requestFields(
                                fieldWithPath("[].category").type(JsonFieldType.STRING).description("카테고리"),
                                fieldWithPath("[].goalAmount").type(JsonFieldType.NUMBER).description("목표 금액")
                        )
                ));
    }

    @Test
    @DisplayName("챌린지 API - 참여 내역 페이징 조회")
    void getParticipateList() throws Exception {
        //given
        for(int i=0;i<5;i++) {
            //7일 주기로 챌린지 5개 생성
            Challenge challenge = challengeRepository.save(
                    Challenge.builder()
                            .startAt(LocalDateTime.now().plusDays(i*7))
                            .endAt(LocalDateTime.now().plusDays((i*7)+7))
                            .build()
            );

            Participation participation = participationRepository.save(
                    Participation.builder()
                            .challenge(challenge)
                            .goalAmount(10000L)
                            .member(memberRepository.getReferenceById(testMemberId))
                            .build()
            );

            //create two plans
            Plan plan1 = planRepository.save(
                    Plan.builder()
                            .category(PlanCategory.TRANSPORTATION)
                            .categoryGoalAmount(10000L*i)
                            .participation(participation)
                            .build()
            );
            plan1.plusCategorySpendAmount(10000L*i);
            Plan plan2 = planRepository.save(
                    Plan.builder()
                            .category(PlanCategory.TRANSPORTATION)
                            .categoryGoalAmount(10000L*i)
                            .participation(participation)
                            .build()
            );
            plan2.plusCategorySpendAmount(10000L*i);
            planRepository.saveAll(List.of(plan1, plan2));
        }
        //when
        //then
        mockMvc.perform(get("/v1/challenge/participate")
                .header(AUTHORIZATION_HEADER, DEVICE_ID)
                .header("Content-Type", "application/json")
                .param("cursor", (String) null)
                .param("size", "10"))
                .andExpect(status().isOk())
                .andDo(document("challenge/participate/GET",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER).description("(필수) device id")
                        ),
                        queryParameters(
                                parameterWithName("cursor").description("(선택) 마지막으로 받은 challengeId. 초기 요청이라면 null 처리"),
                                parameterWithName("size").description("(필수) 페이지 사이즈")
                        ),
                        responseFields(
                                fieldWithPath("[].challengeId").type(JsonFieldType.NUMBER).description("챌린지 id"),
                                fieldWithPath("[].startAt").type(JsonFieldType.STRING).description("종료 시간"),
                                fieldWithPath("[].endAt").type(JsonFieldType.STRING).description("시작 시간"),
                                fieldWithPath("[].month").type(JsonFieldType.NUMBER).description("M째주"),
                                fieldWithPath("[].week").type(JsonFieldType.NUMBER).description("W째주"),
                                fieldWithPath("[].state").type(JsonFieldType.STRING).description("챌린지 상태(PRE_OPENED, OPENED, CLOSED)"),
                                fieldWithPath("[].participationId").type(JsonFieldType.NUMBER).description("참여 id"),
                                fieldWithPath("[].goalAmount").type(JsonFieldType.NUMBER).description("총 목표 금액"),
                                fieldWithPath("[].currentAmount").type(JsonFieldType.NUMBER).description("현재 소비 금액"),
                                fieldWithPath("[].planList[].planId").type(JsonFieldType.NUMBER).description("카테고리별 계획 id"),
                                fieldWithPath("[].planList[].category").type(JsonFieldType.STRING).description("카테고리 이름"),
                                fieldWithPath("[].planList[].categoryGoalAmount").type(JsonFieldType.NUMBER).description("카테고리별 목표 금액"),
                                fieldWithPath("[].planList[].categorySpendAmount").type(JsonFieldType.NUMBER).description("카테고리별 소비 금액")
                        )
                ));
    }
}
