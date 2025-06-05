package com.ssafy.witch.user.command;

import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ChangeUserNicknameCommandTest {
  //1. 닉네임 포맷이 틀려서 예외 발생
  //2. 닉네임 포맷 정상이라서 정상 작동(예외X)
  @Test
  void 닉네임_포맷이_틀리면_커맨드를_생성할_수_없다() {
    //given
    String userId = "1";
    String nickname = "a";
    //when%then
    Assertions.assertThatCode(() -> {
      new ChangeUserNicknameCommand(userId, nickname);
    }).isInstanceOf(ConstraintViolationException.class);
  }

  @Test
  void 닉네임_포맷이_정상이면_커맨드를_생성할_수_있다() {
    //given
    String userId = "1";
    String nickname = "aaaa32a";
    //when%then
    Assertions.assertThatCode(() -> {
      new ChangeUserNicknameCommand(userId, nickname);
    }).doesNotThrowAnyException();
  }
}