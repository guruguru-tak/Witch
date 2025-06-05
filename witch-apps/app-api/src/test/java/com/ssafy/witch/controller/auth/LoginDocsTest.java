package com.ssafy.witch.controller.auth;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
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
import com.ssafy.witch.jwt.JwtProperties;
import com.ssafy.witch.jwt.JwtService;
import com.ssafy.witch.jwt.response.TokenResponse;
import com.ssafy.witch.notification.UpdateFcmTokenUseCase;
import com.ssafy.witch.support.docs.SecurityRestDocsTestSupport;
import com.ssafy.witch.user.WitchUserDetails;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    }
)
class LoginDocsTest extends SecurityRestDocsTestSupport {

  @MockBean
  private UserDetailsService userDetailsService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @MockBean
  private JwtService jwtService;

  @MockBean
  private JwtProperties jwtProperties;

  @MockBean
  private UpdateFcmTokenUseCase fcmTokenUseCase;

  @Test
  void login_200() throws Exception {

    String userId = "user-id";
    String email = "test@test.com";
    String password = "sample_password";
    String fcmToken = "sample_fcm-token";
    List<String> roles = List.of("USER");

    Map<String, String> loginRequest
        = Map.of("email", email,
        "password", password,
        "fcmToken", fcmToken);

    given(userDetailsService.loadUserByUsername(any())).willReturn(WitchUserDetails.builder()
        .userId(userId)
        .email(email)
        .password(passwordEncoder.encode(password))
        .roles(roles)
        .build());

    given(jwtService.create(any(), any(), any())).willReturn(TokenResponse.create(
        "access.token.example",
        3600L,
        "refresh.token.example",
        36000L,
        3600L
    ));

    mvc.perform(
            post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("data.tokenType").value("Bearer"))
        .andExpect(jsonPath("data.accessToken").value("access.token.example"))
        .andExpect(jsonPath("data.accessTokenExpiresIn").value("3600"))
        .andExpect(jsonPath("data.refreshToken").value("refresh.token.example"))
        .andExpect(jsonPath("data.refreshTokenExpiresIn").value("36000"))
        .andDo(restDocs.document(
            requestFields(
                fieldWithPath("email")
                    .type(STRING)
                    .description("사용자 이메일"),
                fieldWithPath("password")
                    .type(STRING)
                    .description("사용자 패스워드"),

                fieldWithPath("fcmToken")
                    .type(STRING)
                    .description("등록할 FCM TOKEN")
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
                    .description("access token 유효 시간 (초단위)"),
                fieldWithPath("data.refreshToken")
                    .type(STRING)
                    .description("access token 재발급을 위한 토큰"),
                fieldWithPath("data.refreshTokenExpiresIn")
                    .type(NUMBER)
                    .description("refresh token 유효 시간 (초단위)"),
                fieldWithPath("data.refreshTokenRenewAvailableSeconds")
                    .type(NUMBER)
                    .description("refresh token 만료 이전 재발급 가능 유효 시간 (초단위)")
            )
        ));
  }

  @Test
  void login_401() throws Exception {

    String email = "test@test.com";
    String password = "sample_password";
    String fcmToken = "sample_fcm-token";

    Map<String, String> loginRequest
        = Map.of("email", email,
        "password", password,
        "fcmToken", fcmToken);

    given(userDetailsService.loadUserByUsername(any())).willThrow(UsernameNotFoundException.class);
    mvc.perform(
            post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
        .andExpect(status().isUnauthorized())
        .andDo(restDocs.document());
  }

}
