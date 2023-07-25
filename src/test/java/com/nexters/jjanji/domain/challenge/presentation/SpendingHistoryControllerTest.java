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
import com.nexters.jjanji.domain.challenge.dto.request.SpendingEditDto;
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
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
        challenge.openChallenge();
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
                                headerWithName(AUTHORIZATION_HEADER).description("(필수) device Id")
                        ),
                        pathParameters(
                                parameterWithName("planId").description("(필수) planId(PK)")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("memo").type(JsonFieldType.STRING).description("메모"),
                                fieldWithPath("spendAmount").type(JsonFieldType.NUMBER).description("소비 금액")
                        )
                ));
    }

    @Test
    @DisplayName("지출 내역 수정 API 호출 성공")
    void editSpendingHistory() throws Exception {
        //given
        final Long amount = 1000L;
        final Long editAmount = 500L;
        Plan plan = planRepository.findById(planId).get();
        SpendingHistory spending = createSpendingHistory(plan, "커피", "스타벅스", amount);
        plan.plusCategorySpendAmount(spending.getSpendAmount());

        final SpendingEditDto dto = new SpendingEditDto("커피", "엔젤리너스", editAmount);

        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/v1/challenge/plan/{planId}/spending/{spendingId}", planId, spending.getId())
                        .header(AUTHORIZATION_HEADER, DEVICE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(dto)))
                .andExpect(status().isOk())
                //Rest docs 문서화
                .andDo(document("challenge/plan/spending/PUT",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER).description("(필수) device Id")
                        ),
                        pathParameters(
                                parameterWithName("planId").description("(필수) planId(PK)"),
                                parameterWithName("spendingId").description("(필수) spendingId(PK)")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("(필수) 변경할 제목"),
                                fieldWithPath("memo").type(JsonFieldType.STRING).description("(선택) 변경할 메모"),
                                fieldWithPath("spendAmount").type(JsonFieldType.NUMBER).description("(필수) 변경할 소비 금액")
                        )
                ));
    }

    @Test
    @DisplayName("지출 내역 리스트 API 호출 성공")
    void findSpendingList() throws Exception {
        //given
        final Integer size = 3;
        final Long amount1 = 500L;
        final Long amount2 = 800L;
        final Long amount3 = 1000L;
        final Long amount4 = 1100L;
        final Long amount5 = 1200L;
        Plan plan = planRepository.findById(planId).get();

        //
        SpendingHistory spending1 = createSpendingHistory(plan, "커피", "스타벅스", amount1);
        plan.plusCategorySpendAmount(spending1.getSpendAmount());
        SpendingHistory spending2 = createSpendingHistory(plan, "커피", "엔젤리너스", amount2);
        plan.plusCategorySpendAmount(spending2.getSpendAmount());
        SpendingHistory spending3 = createSpendingHistory(plan, "커피", "투썸", amount3);
        plan.plusCategorySpendAmount(spending3.getSpendAmount());
        SpendingHistory spending4 = createSpendingHistory(plan, "커피", "백다방", amount4);
        plan.plusCategorySpendAmount(spending4.getSpendAmount());
        SpendingHistory spending5 = createSpendingHistory(plan, "커피", "mess", amount5);
        plan.plusCategorySpendAmount(spending5.getSpendAmount());


        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/v1/challenge/plan/{planId}/spending", planId)
                        .header(AUTHORIZATION_HEADER, DEVICE_ID)
                        .param("cursorId", String.valueOf(spending4.getId()))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.goalAmount").value(plan.getCategoryGoalAmount()))
                .andExpect(jsonPath("$.spendAmount").value(amount1+amount2+amount3+amount4+amount5))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.spendingList[0].spendingId").value(spending3.getId()))
                .andExpect(jsonPath("$.spendingList[0].title").value(spending3.getTitle()))
                .andExpect(jsonPath("$.spendingList[0].memo").value(spending3.getMemo()))
                .andExpect(jsonPath("$.spendingList[0].spendAmount").value(spending3.getSpendAmount()))
                .andExpect(jsonPath("$.spendingList[1].spendingId").value(spending2.getId()))
                .andExpect(jsonPath("$.spendingList[1].title").value(spending2.getTitle()))
                .andExpect(jsonPath("$.spendingList[1].memo").value(spending2.getMemo()))
                .andExpect(jsonPath("$.spendingList[1].spendAmount").value(spending2.getSpendAmount()))
                .andExpect(jsonPath("$.spendingList[2].spendingId").value(spending1.getId()))
                .andExpect(jsonPath("$.spendingList[2].title").value(spending1.getTitle()))
                .andExpect(jsonPath("$.spendingList[2].memo").value(spending1.getMemo()))
                .andExpect(jsonPath("$.spendingList[2].spendAmount").value(spending1.getSpendAmount()))
                .andDo(print())
                //Rest docs 문서화
                .andDo(document("challenge/plan/spending/GET",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER).description("(필수) device Id")
                        ),
                        pathParameters(
                                parameterWithName("planId").description("(필수) planId(PK)")
                        ),
                        queryParameters(
                                parameterWithName("cursorId").description("(선택) 마지막 spendingId(PK) 값, 첫 요청이라면 NULL"),
                                parameterWithName("size").description("(필수) 페이지 사이즈")
                        ),
                        responseFields(
                                fieldWithPath("goalAmount").type(JsonFieldType.NUMBER).description("이번주 카테고리 목표 금액"),
                                fieldWithPath("spendAmount").type(JsonFieldType.NUMBER).description("현재 카테고리 총 지출 금액"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 소비 내역 존배 여부"),
                                fieldWithPath("spendingList[].spendingId").type(JsonFieldType.NUMBER).description("지출 내역 Id(PK)"),
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
