package com.nexters.jjanji.domain.challenge.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.jjanji.docs.RestDocs;
import com.nexters.jjanji.domain.challenge.application.SpendingHistoryService;
import com.nexters.jjanji.domain.challenge.domain.Challenge;
import com.nexters.jjanji.domain.challenge.domain.Participation;
import com.nexters.jjanji.domain.challenge.domain.Plan;
import com.nexters.jjanji.domain.challenge.domain.repository.ChallengeRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.ParticipationRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.PlanRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.SpendingHistoryRepository;
import com.nexters.jjanji.domain.challenge.dto.request.SpendingSaveDto;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
class SpendingHistoryControllerTest extends RestDocs {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    SpendingHistoryService spendingHistoryService;
    @Autowired
    SpendingHistoryRepository spendingHistoryRepository;
    @Autowired
    ChallengeRepository challengeRepository;
    @Autowired
    PlanRepository planRepository;
    @Autowired
    ParticipationRepository participationRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    SpendingHistoryController spendingHistoryController;
    @Autowired
    HandlerInterceptor handlerInterceptor;

    MockMvc mockMvc;
    final String AUTHORIZATION_HEADER = "Authorization";
    final String DEVICE_ID = "spending_device_id";
    Long planId;
    @BeforeEach
    void setUp(RestDocumentationContextProvider provider) {
        mockMvc = getMockMvcBuilder(provider, spendingHistoryController)
                .addInterceptors(handlerInterceptor)
                .build();

        Member member = memberRepository.save(Member.builder().deviceId(DEVICE_ID).build());
        Challenge challenge = challengeRepository.save(Challenge.builder().startAt(LocalDateTime.now()).endAt(LocalDateTime.now().plusDays(7)).build());
        Participation participation = participationRepository.save(Participation.builder().member(member).challenge(challenge).goalAmount(10000L).build());
        planId = planRepository.save(Plan.builder().participation(participation).category(PlanCategory.FOOD).categoryGoalAmount(5|000L).build()).getId();
    }

    @Test
    @DisplayName("지출 내역 등록 API 호출 성공")
    void addSpendingHistory() throws Exception {
        //given
        final SpendingSaveDto dto = new SpendingSaveDto("title", "memo", 5000L);

        //when & then
        //Path 관련 Rest docs 문서 생성시 RestDocumentationRequestBuilders 사용하기.
        mockMvc.perform(RestDocumentationRequestBuilders.post("/v1/challenge/plan/{planId}/spending", planId)
                        .header(AUTHORIZATION_HEADER, DEVICE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(dto)))
                .andExpect(status().isOk())
                //Rest docs 문서화
                .andDo(document("challenge/plan/spending/POST",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER).description("device Id (필수값)")
                        ),
                        pathParameters(
                                parameterWithName("planId").description("plan 고유 pk")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("memo").type(JsonFieldType.STRING).description("메모"),
                                fieldWithPath("spendAmount").type(JsonFieldType.NUMBER).description("소비 금액")
                        )
                ));
    }

    private <T> String toJson(T data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }
}
