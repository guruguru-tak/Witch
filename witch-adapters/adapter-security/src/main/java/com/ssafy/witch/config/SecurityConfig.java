package com.ssafy.witch.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.witch.entrypoint.JwtAuthenticationEntryPoint;
import com.ssafy.witch.filter.AccessTokenReissueFilter;
import com.ssafy.witch.filter.JsonLoginProcessingFilter;
import com.ssafy.witch.filter.JwtAuthenticationProcessingFilter;
import com.ssafy.witch.filter.RefreshTokenRenewFilter;
import com.ssafy.witch.handler.ErrorResponseAuthenticationFailureHandler;
import com.ssafy.witch.handler.JwtAuthenticationSuccessHandler;
import com.ssafy.witch.jwt.JwtService;
import com.ssafy.witch.notification.UpdateFcmTokenUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

  private final ObjectMapper objectMapper;

  private final JwtService jwtService;

  private final AuthenticationConfiguration authenticationConfiguration;

  private final UpdateFcmTokenUseCase fcmTokenUseCase;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.httpBasic(AbstractHttpConfigurer::disable);
    http.formLogin(AbstractHttpConfigurer::disable);
    http.csrf(AbstractHttpConfigurer::disable);
    http.sessionManagement(
        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http.anonymous(AbstractHttpConfigurer::disable);

    http.authorizeHttpRequests(auth -> {
      auth
          .requestMatchers(HttpMethod.GET, "/users/email/is-unique").permitAll()
          .requestMatchers(HttpMethod.GET, "/users/nickname/is-unique").permitAll()
          .requestMatchers(HttpMethod.POST, "/users/email-verification-code").permitAll()
          .requestMatchers(HttpMethod.POST, "/users/email-verification-code/confirm").permitAll()
          .requestMatchers(HttpMethod.POST, "/users").permitAll()
          .requestMatchers(HttpMethod.GET, "/groups/{groupId}/preview").permitAll()
          .requestMatchers(HttpMethod.GET, "/deeplink").permitAll()
          .anyRequest().authenticated();
    });

    http.exceptionHandling(exceptionHandling ->
        exceptionHandling.authenticationEntryPoint(authenticationEntryPoint()));

    http.addFilterAt(jsonLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
    http.addFilterBefore(jwtAuthenticationProcessingFilter(),
        UsernamePasswordAuthenticationFilter.class);
    http.addFilterBefore(accessTokenReissueFilter(), UsernamePasswordAuthenticationFilter.class);
    http.addFilterBefore(refreshTokenRenewFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public AccessTokenReissueFilter accessTokenReissueFilter() {
    return new AccessTokenReissueFilter(jwtService, objectMapper);
  }

  @Bean
  public RefreshTokenRenewFilter refreshTokenRenewFilter() {
    return new RefreshTokenRenewFilter(jwtService, objectMapper);
  }

  @Bean
  public AbstractAuthenticationProcessingFilter jsonLoginProcessingFilter() throws Exception {
    JsonLoginProcessingFilter jsonLoginProcessingFilter =
        new JsonLoginProcessingFilter(objectMapper);

    jsonLoginProcessingFilter.setAuthenticationManager(authenticationManager());
    jsonLoginProcessingFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
    jsonLoginProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler());

    return jsonLoginProcessingFilter;
  }


  @Bean
  public AuthenticationSuccessHandler authenticationSuccessHandler() {
    return new JwtAuthenticationSuccessHandler(jwtService, objectMapper, fcmTokenUseCase);
  }

  @Bean
  public AuthenticationFailureHandler authenticationFailureHandler() {
    return new ErrorResponseAuthenticationFailureHandler(objectMapper);
  }

  @Bean
  public AuthenticationManager authenticationManager() throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  AuthenticationEntryPoint authenticationEntryPoint() {
    return new JwtAuthenticationEntryPoint(objectMapper);
  }

  @Bean
  JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
    return new JwtAuthenticationProcessingFilter(jwtService);
  }
}
