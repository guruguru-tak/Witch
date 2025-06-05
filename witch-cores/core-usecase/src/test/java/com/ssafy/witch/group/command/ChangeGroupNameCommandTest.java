package com.ssafy.witch.group.command;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ChangeGroupNameCommandTest {

  //1. 변경할 그룹 이름 포맷 틀려서 예외 발생
  //2. 변경할 그룹 이름 포맷 정상이라서 통과

  @Test
  void 변경할_그룹_이름의_포맷이_틀리면_커맨드를_생성할_수_없다() {
    //given
    String userId = "test-user-id";
    String groupId = "test-group-id";
    String name = "testnamee11";

    //when&then
    Assertions.assertThatThrownBy(()->{
      new ChangeGroupNameCommand(userId, groupId, name);
    }).isInstanceOf(ConstraintViolationException.class);
  }

  @Test
  void 변경할_그룹_이름의_포맷이_정상이면_커맨드를_생성할_수_있다() {
    //given
    String userId = "test-user-id";
    String groupId = "test-group-id";
    String name = "testname";

    //when&then
    Assertions.assertThatCode(()->{
      new ChangeGroupNameCommand(userId, groupId, name);
    }).doesNotThrowAnyException();
  }
}