package com.ssafy.witch.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.witch.authentication.FcmTokenWebAuthenticationDetails;
import com.ssafy.witch.jwt.JwtService;
import com.ssafy.witch.jwt.response.TokenResponse;
import com.ssafy.witch.notification.UpdateFcmTokenUseCase;
import com.ssafy.witch.user.WitchUserDetails;
import com.ssafy.witch.utils.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
public class JwtAuthenticationSuccessHandler implements
    AuthenticationSuccessHandler {

  private final JwtService jwtService;
  private final ObjectMapper objectMapper;
  private final UpdateFcmTokenUseCase updateFcmTokenUseCase;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    WitchUserDetails witchUserDetails = (WitchUserDetails) authentication.getPrincipal();
    FcmTokenWebAuthenticationDetails details = (FcmTokenWebAuthenticationDetails) authentication.getDetails();

    String fcmToken = details.getFcmToken();
    String userId = witchUserDetails.getUserId();
    String email = witchUserDetails.getEmail();
    List<String> roles = witchUserDetails.getRoles();

    TokenResponse tokenResponse = jwtService.create(userId, email, roles);

    updateFcmTokenUseCase.update(userId, fcmToken);

    ResponseUtils.sendResponse(response, tokenResponse, objectMapper);
  }


}
