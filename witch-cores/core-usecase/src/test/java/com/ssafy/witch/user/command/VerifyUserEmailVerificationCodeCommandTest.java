package com.ssafy.witch.user.command;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VerifyUserEmailVerificationCodeCommandTest {

  //1. 이메일 인증을 요청한 이메일의 포맷이 틀린 경우
  //2. 이메일 인증을 요청한 인증 코드 포맷이 틀린 경우
  //3. 이메일과 인증코드의 포맷이 정상인 경우

  @Test
  void 이메일_인증을_요청한_이메일의_포맷이_틀린_경우() {
    //given
    String email = "testtestcom";
    String code = "123456";

    //when&then
    Assertions.assertThatThrownBy(()->{
      VerifyUserEmailVerificationCodeCommand command = new VerifyUserEmailVerificationCodeCommand(
          email, code);
    }).isInstanceOf(ConstraintViolationException.class);
  }

  @Test
  void 이메일_인증을_요청한_인증_코드_포맷이_틀린_경우() {
    //given
    String email = "test@test.com";
    String code = "1234";

    //when&then
    Assertions.assertThatThrownBy(()->{
      VerifyUserEmailVerificationCodeCommand command = new VerifyUserEmailVerificationCodeCommand(
          email, code);
    }).isInstanceOf(ConstraintViolationException.class);
  }

  @Test
  void 이메일_인증을_요청한_이메일과_인증_코드의_포맷이_정상인_경우() {
    //given
    String email = "test@test.com";
    String code = "123456";

    //when&then
    Assertions.assertThatCode(()->{
      VerifyUserEmailVerificationCodeCommand command = new VerifyUserEmailVerificationCodeCommand(
          email, code);
    }).doesNotThrowAnyException();
  }
}