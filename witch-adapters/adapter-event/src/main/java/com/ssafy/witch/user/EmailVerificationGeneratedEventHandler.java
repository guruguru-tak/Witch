package com.ssafy.witch.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class EmailVerificationGeneratedEventHandler {

  private final EmailVerificationCodeMailPort emailVerificationCodeMailPort;

  @Async
  @EventListener
  public void handleEmailVerificationCodeGeneratedEvent(EmailVerificationCodeGeneratedEvent event) {
    log.info("handler");
    emailVerificationCodeMailPort.send(event.getEmail(), event.getCode());
  }
}
