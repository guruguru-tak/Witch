package com.ssafy.witch.group;

import static org.junit.jupiter.api.Assertions.*;

import com.ssafy.witch.exception.group.GroupNameDuplicatedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class GroupValidateServiceTest {

  @InjectMocks
  private GroupValidateService sut;

  @Mock
  private ValidateGroupPort validateGroupPort;

  //1. 중복 검사를 요청한 이름의 포맷이 잘못된 경우 -> 검증해야함?
  //2. 요청한 이름이 중복이라서 중복 예외가 발생하는 경우
  //3. 중복 검사 정상적으로 검사에 성공하는 경우

  @Test
  void 요청한_그룹_이름이_중복인_경우_검사에_실패한다() {
    //given
    String groupName = "구미푸파";

    BDDMockito
        .given(validateGroupPort.isNameDuplicated(groupName))
        .willReturn(true);
    //when&then
    Assertions.assertThatThrownBy(() -> {
      sut.checkGroupNameDuplication(groupName);
    }).isInstanceOf(GroupNameDuplicatedException.class);
  }

  @Test
  void 요청한_그룹_이름이_중복이_아닌_경우_검사에_성공한다() {
    //given
    String groupName = "구미푸파";

    BDDMockito
        .given(validateGroupPort.isNameDuplicated(groupName))
        .willReturn(false);
    //when&then
    Assertions.assertThatCode(()->{
      sut.checkGroupNameDuplication(groupName);
    }).doesNotThrowAnyException();
  }
}