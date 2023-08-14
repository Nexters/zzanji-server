package com.nexters.jjanji.global.auth;

import com.nexters.jjanji.docs.RestDocs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class AuthControllerTest extends RestDocs {
    MockMvc mockMvc;
    @Autowired AuthController authController;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = getMockMvcBuilder(restDocumentation, authController).build();
    }

    @Test
    void validateRequiredAppVersion() throws Exception {
        mockMvc.perform(get("/v1/auth/version")
                        .param("os", "Android")
                        .param("appVersion", "0.5.0"))
                .andExpect(status().isOk())
                .andDo(document("auth/version/GET",
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("os").description("(필수) 운영체제 ex) iOS / Android"),
                                parameterWithName("appVersion").description("(필수) 앱의 버전 ex) 0.5")
                        )
                ));
    }

    @Test
    void validateRequiredAppVersion_fail() throws Exception {
        mockMvc.perform(get("/v1/auth/version")
                .param("os", "Android")
                .param("appVersion", "0.1.0"))
                .andExpect(status().isUpgradeRequired())
                .andDo(document("auth/version/GET/fail",
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("os").description("(필수) 운영체제 ex) iOS / Android"),
                                parameterWithName("appVersion").description("(필수) 앱의 버전 ex) 0.5")
                        )
                ));
    }
}