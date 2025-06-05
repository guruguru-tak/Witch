package com.ssafy.witch.user.command;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolationException;
import java.beans.Transient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CreateUserEmailVerificationCodeCommandTest {

  //1. 이메일 포맷이 틀리면 커맨드를 생성할 수 없다
  //2. 이메일 포맷이 정상이면 커맨드가 생성된다.

  @Test
  void 이메일_포맷이_틀리면_커맨드를_생성할_수_없다() {
    //given
    String email = "test";
    //when&then
    Assertions.assertThatThrownBy(()-> {
      CreateUserEmailVerificationCodeCommand command = new CreateUserEmailVerificationCodeCommand(email);
    }).isInstanceOf(ConstraintViolationException.class);
  }

  @Test
  void 이메일_포맷이_정상이면_커맨드가_생성된다() {
    //given
    String email = "test@test.com";
    //when&then
    Assertions.assertThatCode(()->{
      CreateUserEmailVerificationCodeCommand command = new CreateUserEmailVerificationCodeCommand(email);
    }).doesNotThrowAnyException();
  }

}