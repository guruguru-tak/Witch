package com.ssafy.witch.mock.user;

import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockWitchUserSecurityContextFactory implements
    WithSecurityContextFactory<WithMockWitchUser> {


  @Override
  public SecurityContext createSecurityContext(WithMockWitchUser annotation) {
    final SecurityContext context = SecurityContextHolder.createEmptyContext();

    List<SimpleGrantedAuthority> roles = List.of(new SimpleGrantedAuthority("ROLE_USER"));

    UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.authenticated(
        annotation.userId(), null, roles);

    context.setAuthentication(token);
    return context;
  }
}
