package com.ssafy.witch.user;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailVerificationMailSender implements EmailVerificationCodeMailPort {

  private static final String SUBJECT = "[Witch] 회원 가입을 위해 메일을 인증해 주세요.";

  private final JavaMailSender mailSender;
  private final EmailVerificationCodeHtmlLoader htmlLoader;

  @Override
  public void send(String email, EmailVerificationCode code) {
    MimeMessage mimeMessage = mailSender.createMimeMessage();

    MimeMessageHelper helper = null;
    try {
      helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
      helper.setTo(email);
      helper.setSubject(SUBJECT);
      helper.setText(htmlLoader.loadWith(code), true);
    } catch (MessagingException e) {
      log.error(e.getMessage(), e);
    }

    mailSender.send(mimeMessage);
  }

}
