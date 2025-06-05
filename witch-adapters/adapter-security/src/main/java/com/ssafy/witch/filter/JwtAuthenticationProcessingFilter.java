package com.ssafy.witch.filter;

import com.ssafy.witch.exception.auth.InvalidAccessTokenException;
import com.ssafy.witch.jwt.JwtService;
import com.ssafy.witch.user.WitchUserDetails;
import com.ssafy.witch.utils.JwtConst;
import com.ssafy.witch.utils.TokenExtractUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

  private static final String ACCESS_TOKEN_HEADER_NAME = "Authorization";

  private final JwtService jwtService;

  private static UsernamePasswordAuthenticationToken getAuthenticated(
      WitchUserDetails witchUserDetails) {
    return UsernamePasswordAuthenticationToken.authenticated(
        witchUserDetails.getUserId(), null,
        witchUserDetails.getRoles().stream().map(SimpleGrantedAuthority::new).toList());
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String accessToken = TokenExtractUtils.extractAccessTokenFromHeader(request);

    if (!StringUtils.hasText(accessToken)) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      WitchUserDetails witchUserDetails = jwtService.resolveAccessToken(accessToken);
      Authentication authentication = getAuthenticated(witchUserDetails);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (InvalidAccessTokenException e) {
      request.setAttribute(JwtConst.JWT_AUTH_EXCEPTION_ATTRIBUTE_KEY, e);
    }

    filterChain.doFilter(request, response);
  }
}
