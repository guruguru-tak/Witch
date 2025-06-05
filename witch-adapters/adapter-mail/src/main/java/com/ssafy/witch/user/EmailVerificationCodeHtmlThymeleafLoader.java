package com.ssafy.witch.user;

import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@RequiredArgsConstructor
@Component
public class EmailVerificationCodeHtmlThymeleafLoader implements EmailVerificationCodeHtmlLoader {

  private static final String TEMPLATE = "email-verification-code-template";

  private final SpringTemplateEngine templateEngine;

  @Override
  public String loadWith(EmailVerificationCode code) {
    HashMap<String, String> emailValues = new HashMap<>();
    emailValues.put("emailVerificationCode", code.getCode());

    Context context = new Context();
    emailValues.forEach(context::setVariable);

    return templateEngine.process(TEMPLATE, context);
  }
}
