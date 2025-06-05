package com.ssafy.witch.authentication;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@Getter
public class FcmTokenWebAuthenticationDetails extends WebAuthenticationDetails {

  private final String fcmToken;

  public FcmTokenWebAuthenticationDetails(HttpServletRequest request, String fcmToken) {
    super(request);
    this.fcmToken = fcmToken;
  }
}
