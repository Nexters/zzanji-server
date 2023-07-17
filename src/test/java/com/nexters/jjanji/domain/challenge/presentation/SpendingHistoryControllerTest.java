package com.nexters.jjanji.domain.challenge.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.jjanji.docs.RestDocs;
import com.nexters.jjanji.domain.challenge.application.SpendingHistoryService;
import com.nexters.jjanji.domain.challenge.domain.Challenge;
import com.nexters.jjanji.domain.challenge.domain.Participation;
import com.nexters.jjanji.domain.challenge.domain.Plan;
import com.nexters.jjanji.domain.challenge.domain.SpendingHistory;
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
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
                .addFilter(new CharacterEncodingFilter("UTF-8", true)) //한글 깨짐
                .build();

        Member member = memberRepository.save(Member.builder().deviceId(DEVICE_ID).build());
        Challenge challenge = challengeRepository.save(Challenge.builder().startAt(LocalDateTime.now()).endAt(LocalDateTime.now().plusDays(7)).build());
        Participation participation = participationRepository.save(Participation.builder().member(member).challenge(challenge).goalAmount(10000L).build());
        planId = planRepository.save(Plan.builder().participation(participation).category(PlanCategory.FOOD).categoryGoalAmount(5000L).build()).getId();
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

    @Test
    @DisplayName("지출 내역 리스트 API 호출 성공")
    void findSpendingList() throws Exception {
        //given
        final Long amount1 = 1000L;
        final Long amount2 = 2000L;
        Plan plan = planRepository.findById(planId).get();

        SpendingHistory spending1 = createSpendingHistory(plan, "커피", "스타벅스", amount1);
        plan.plusCategorySpendAmount(spending1.getSpendAmount());

        SpendingHistory spending2 = createSpendingHistory(plan, "커피", "엔젤리너스", amount2);
        plan.plusCategorySpendAmount(spending2.getSpendAmount());

        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/v1/challenge/plan/{planId}/spending", planId)
                        .header(AUTHORIZATION_HEADER, DEVICE_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.goalAmount").value(plan.getCategoryGoalAmount()))
                .andExpect(jsonPath("$.spendAmount").value(amount1+amount2))
                .andExpect(jsonPath("$.spendingList[0].title").value(spending1.getTitle()))
                .andExpect(jsonPath("$.spendingList[0].memo").value(spending1.getMemo()))
                .andExpect(jsonPath("$.spendingList[0].spendAmount").value(spending1.getSpendAmount()))
                .andExpect(jsonPath("$.spendingList[1].title").value(spending2.getTitle()))
                .andExpect(jsonPath("$.spendingList[1].memo").value(spending2.getMemo()))
                .andExpect(jsonPath("$.spendingList[1].spendAmount").value(spending2.getSpendAmount()))
                .andDo(print())
                //Rest docs 문서화
                .andDo(document("challenge/plan/spending/GET",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER).description("device Id (필수값)")
                        ),
                        pathParameters(
                                parameterWithName("planId").description("plan 고유 pk")
                        ),
                        responseFields(
                                fieldWithPath("goalAmount").type(JsonFieldType.NUMBER).description("이번주 카테고리 목표 금액"),
                                fieldWithPath("spendAmount").type(JsonFieldType.NUMBER).description("현재 카테고리 총 지출 금액"),
                                fieldWithPath("spendingList[].title").type(JsonFieldType.STRING).description("지출 내역 제목"),
                                fieldWithPath("spendingList[].memo").type(JsonFieldType.STRING).description("지출 내역 메모"),
                                fieldWithPath("spendingList[].spendAmount").type(JsonFieldType.NUMBER).description("지출 내역 금액")
                        )
                ));
    }

    private SpendingHistory createSpendingHistory(Plan plan, String title, String memo, Long amount){
        SpendingHistory spendingHistory = SpendingHistory.builder()
                .plan(plan)
                .title(title)
                .memo(memo)
                .spendAmount(amount)
                .build();
        return spendingHistoryRepository.save(spendingHistory);
    }
    private <T> String toJson(T data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }
}
