package com.ssafy.witch.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailVerificationGeneratedEventPublisher implements
    EmailVerificationGeneratedEventPort {

  private final ApplicationEventPublisher applicationEventPublisher;

  @Override
  public void publish(EmailVerificationCodeGeneratedEvent event) {
    log.info("publish");
    applicationEventPublisher.publishEvent(event);
  }
}
