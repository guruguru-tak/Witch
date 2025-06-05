package com.ssafy.witch.controller.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.witch.config.PasswordEncoderConfig;
import com.ssafy.witch.config.SecurityConfig;
import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.auth.InvalidRefreshTokenException;
import com.ssafy.witch.jwt.JwtProperties;
import com.ssafy.witch.jwt.JwtService;
import com.ssafy.witch.jwt.response.TokenResponse;
import com.ssafy.witch.notification.UpdateFcmTokenUseCase;
import com.ssafy.witch.support.docs.SecurityRestDocsTestSupport;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

@WebMvcTest(
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Controller.class)
    }
)
@Import(
    {
        SecurityConfig.class,
        PasswordEncoderConfig.class,
        JwtProperties.class
    }
)
public class TokenReissueDocsTest extends SecurityRestDocsTestSupport {

  @MockBean
  private UpdateFcmTokenUseCase updateFcmTokenUseCase;

  @MockBean
  private UserDetailsService userDetailsService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @MockBean
  private JwtService jwtService;

  @Test
  void reissue_200() throws Exception {

    String refreshToken = "Bearer refresh.token.example";

    Map<String, String> request = new HashMap<>();
    request.put("refreshToken", refreshToken);

    given(jwtService.reissue(any())).willReturn(
        TokenResponse.reissue(
            "access.token.example",
            3600L
        )
    );

    mvc.perform(
            post("/auth/token/reissue")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("success").value(true))
        .andExpect(jsonPath("data.tokenType").value("Bearer"))
        .andExpect(jsonPath("data.accessToken").value("access.token.example"))
        .andExpect(jsonPath("data.accessTokenExpiresIn").value("3600"))
        .andDo(restDocs.document(
            requestFields(
                fieldWithPath("refreshToken")
                    .description("만료 전 refresh token")
            ),
            responseFields(
                fieldWithPath("success")
                    .type(BOOLEAN)
                    .description("성공 여부"),
                fieldWithPath("data.tokenType")
                    .type(STRING)
                    .description("토큰 타입"),
                fieldWithPath("data.accessToken")
                    .type(STRING)
                    .description("JWT access token"),
                fieldWithPath("data.accessTokenExpiresIn")
                    .type(NUMBER)
                    .description("access token 유효 시간 (초단위)")
            )
        ));
  }

  @Test
  void reissue_401() throws Exception {

    String refreshToken = "Bearer refresh.token.example";

    Map<String, String> request = new HashMap<>();
    request.put("refreshToken", refreshToken);

    doThrow(new InvalidRefreshTokenException()).when(jwtService).reissue(any());

    mvc.perform(
            post("/auth/token/reissue")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isUnauthorized())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.INVALID_REFRESH_TOKEN.getErrorCode()))
        .andDo(restDocs.document());
  }


}
